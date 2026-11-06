"""Verify optional addon integrity, reviewable resources and rebuilt member bytes."""

import argparse
import hashlib
import json
from pathlib import Path, PurePosixPath
import re
import zipfile

ROOT = Path(__file__).resolve().parent


def require(condition, message):
    if not condition:
        raise ValueError(message)


def digest(data):
    return hashlib.sha256(data).hexdigest()


def members(path):
    with zipfile.ZipFile(path) as archive:
        require(archive.testzip() is None, f"CRC failure: {path.name}")
        names = archive.namelist()
        require(len(names) == len(set(names)), f"Duplicate archive members: {path.name}")
        for name in names:
            entry = PurePosixPath(name)
            require(not entry.is_absolute() and ".." not in entry.parts and "\\" not in name,
                    f"Unsafe archive member: {name}")
        return {name: archive.read(name) for name in names}


def same_members(expected, actual, label):
    require(expected.keys() == actual.keys(), f"Different member lists: {label}")
    for name, content in expected.items():
        require(content == actual[name], f"Different bytes: {label}/{name}")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--rebuilt", action="store_true", help="Also compare both rebuilt archives")
    options = parser.parse_args()
    manifest = json.loads((ROOT / "manifest.json").read_text(encoding="utf-8"))
    checksum_lines = []
    for name, info in manifest["files"].items():
        content = (ROOT / "packages" / name).read_bytes()
        require(len(content) == info["bytes"] and digest(content) == info["sha256"],
                f"Unexpected shipped artifact: {name}")
        checksum_lines.append(f'{info["sha256"]}  packages/{name}')
    require((ROOT / "SHA256SUMS.txt").read_text(encoding="utf-8").splitlines() == checksum_lines,
            "SHA256SUMS.txt does not match the manifest")

    pack = members(ROOT / "packages" / "StoneBlock4_StoneUI.zip")
    expected = {entry["path"]: entry for entry in manifest["zip_members"]}
    require(len(pack) == 46 and pack.keys() == expected.keys(), "Unexpected resource pack scope")
    for name, content in pack.items():
        info = expected[name]
        require(len(content) == info["bytes"] and digest(content) == info["sha256"],
                f"Unexpected resource bytes: {name}")
        require(name in {"pack.mcmeta", "pack.png"}
                or name.startswith("assets/minecraft/textures/gui/sprites/widget/")
                or name.startswith("assets/stoneblock4_stoneui/"),
                f"Out-of-scope resource: {name}")
        if name.endswith((".json", ".mcmeta")):
            json.loads(content)
    source = ROOT / "resourcepack"
    expanded = {p.relative_to(source).as_posix(): p.read_bytes() for p in source.rglob("*") if p.is_file()}
    same_members(pack, expanded, "resourcepack")
    require(json.loads(pack["pack.mcmeta"])["pack"]["pack_format"] == 34, "Unexpected Minecraft pack format")

    jar = members(ROOT / "packages" / "sb4-stone-ui-bridge.jar")
    require(len([name for name in jar if name.endswith(".class")]) == 6, "Unexpected bridge class count")
    mixin = json.loads(jar["sb4_stone_ui_bridge.mixins.json"])
    require(mixin.get("client") == ["ScreenBackgroundMixin"] and not mixin.get("mixins") and not mixin.get("server"),
            "Bridge must have only the client background mixin")
    require("assets/stoneblock4_stoneui/theme.json" not in jar, "Bridge must not activate its own theme")
    fallback = jar["assets/stoneblock4_stoneui/textures/gui/settings_background.png"]
    require(fallback[:8] == b"\x89PNG\r\n\x1a\n" and fallback[16:24] == b"\0\0\0\1\0\0\0\1",
            "Expected the 1x1 fallback PNG")
    resources = ROOT / "bridge" / "src" / "main" / "resources"
    for path in resources.rglob("*"):
        if path.is_file():
            require(jar.get(path.relative_to(resources).as_posix()) == path.read_bytes(),
                    f"Different bridge resource: {path.name}")
    java = ROOT / "bridge" / "src" / "main" / "java" / "cn" / "codex" / "stoneui"
    scope = (java / "VanillaOptionsScope.java").read_text(encoding="utf-8")
    names = re.findall(r'"(net\.minecraft\.client\.gui\.screens\.[^"]+)"', scope)
    require(len(set(names)) == 12 and "VANILLA_OPTIONS.contains(className)" in scope,
            "Expected 12 exact vanilla settings classes")

    if options.rebuilt:
        same_members(pack, members(ROOT / "build" / "StoneBlock4_StoneUI.zip"), "rebuilt resource pack")
        same_members(jar, members(ROOT / "bridge" / "build" / "sb4-stone-ui-bridge.jar"), "rebuilt bridge")
    print(json.dumps({"status": "pass", "resource_files": len(pack), "bridge_classes": 6,
                      "rebuilt_member_bytes_checked": options.rebuilt, "game_modified": False}))


if __name__ == "__main__":
    main()
