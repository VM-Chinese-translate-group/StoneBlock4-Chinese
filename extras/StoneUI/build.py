"""Package the editable resource pack locally; never installs or enables it."""

from pathlib import Path
import zipfile


def main():
    root = Path(__file__).resolve().parent
    source = root / "resourcepack"
    output = root / "build" / "StoneBlock4_StoneUI.zip"
    output.parent.mkdir(exist_ok=True)
    files = sorted(path for path in source.rglob("*") if path.is_file())
    if not (source / "pack.mcmeta").is_file():
        raise SystemExit("Missing resourcepack/pack.mcmeta")
    with zipfile.ZipFile(output, "w", compression=zipfile.ZIP_DEFLATED) as archive:
        for path in files:
            entry = zipfile.ZipInfo(path.relative_to(source).as_posix(), (2026, 1, 1, 0, 0, 0))
            entry.compress_type = zipfile.ZIP_DEFLATED
            entry.external_attr = 0o100644 << 16
            archive.writestr(entry, path.read_bytes())
    print(f"Built {output.name}: {len(files)} files; no installation performed.")


if __name__ == "__main__":
    main()
