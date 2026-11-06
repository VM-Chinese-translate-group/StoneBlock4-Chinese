"""Offline Java 21 build against the exact installed, named NeoForge runtime; never installs anything."""
from pathlib import Path
import hashlib, json, os, shutil, subprocess, zipfile

ROOT = Path(__file__).resolve().parent
PRISM = Path(os.environ['SB4_PRISM_ROOT']).resolve()
INSTANCE = Path(os.environ.get('SB4_INSTANCE', str(PRISM/'instances/FTB StoneBlock 4/minecraft')))
JDK = Path(os.environ['SB4_JDK']).resolve()
LIB = PRISM/'libraries'
BUILD = ROOT/'build'
CLASSES = BUILD/'classes'

def sha(path): return hashlib.sha256(path.read_bytes()).hexdigest()

def main():
    BUILD.mkdir(exist_ok=True)
    assert CLASSES.resolve().parent == BUILD.resolve() and BUILD.resolve().parent == ROOT.resolve()
    if CLASSES.exists(): shutil.rmtree(CLASSES)
    CLASSES.mkdir()
    # The patched client jar must precede the base named client; it contains NeoForge API additions.
    required = [
        LIB/'net/neoforged/neoforge/21.1.262/neoforge-21.1.262-client.jar',
        LIB/'net/minecraft/client/1.21.1-20240808.144430/client-1.21.1-20240808.144430-srg.jar',
        LIB/'net/neoforged/neoforge/21.1.262/neoforge-21.1.262-universal.jar',
        LIB/'net/neoforged/fancymodloader/loader/4.0.45/loader-4.0.45.jar',
        LIB/'net/fabricmc/sponge-mixin/0.16.4+mixin.0.8.7/sponge-mixin-0.16.4+mixin.0.8.7.jar',
        INSTANCE/'mods/oracle_index-neoforge-1.4.0.jar',
    ]
    for path in required: assert path.is_file(), f'Missing required runtime dependency: {path}'
    support = [p for p in LIB.rglob('*.jar') if p not in required and
               not p.name.startswith(('neoforge-', 'client-', 'forge-')) and
               '/net/minecraft/' not in p.as_posix()]
    classpath = os.pathsep.join(map(str, required + sorted(support)))
    sources = sorted((ROOT/'src/main/java').rglob('*.java'))
    args = ['--release', '21', '-encoding', 'UTF-8', '-proc:none', '-classpath', classpath,
            '-d', str(CLASSES), *map(str, sources)]
    argfile = BUILD/'javac.args'
    argfile.write_text('\n'.join('"'+arg.replace('\\','/').replace('"','\\"')+'"' for arg in args), encoding='utf-8')
    subprocess.run([str(JDK/'bin'/('javac.exe' if os.name == 'nt' else 'javac')), '@'+str(argfile)], check=True)
    artifact = BUILD/'sb4-localization-compat.jar'
    with zipfile.ZipFile(artifact, 'w', compression=zipfile.ZIP_DEFLATED) as jar:
        jar.writestr('META-INF/MANIFEST.MF', 'Manifest-Version: 1.0\nImplementation-Version: 1.0.0\n\n')
        for root in [CLASSES, ROOT/'src/main/resources']:
            for path in sorted(root.rglob('*')):
                if path.is_file(): jar.write(path, path.relative_to(root).as_posix())
    report = {'artifact':str(artifact), 'sha256':sha(artifact), 'java_release':21, 'errors':[],
              'source_files':{str(p.relative_to(ROOT)):sha(p) for p in sources},
              'resource_files':{str(p.relative_to(ROOT)):sha(p) for p in sorted((ROOT/'src/main/resources').rglob('*')) if p.is_file()},
              'required_compile_dependencies':{str(p):sha(p) for p in required},
              'instance_modified':False}
    (BUILD/'build-report.json').write_text(json.dumps(report,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
    print(json.dumps({'artifact':str(artifact),'sha256':report['sha256'],'classes':len(list(CLASSES.rglob('*.class')))},ensure_ascii=False))

if __name__ == '__main__': main()
