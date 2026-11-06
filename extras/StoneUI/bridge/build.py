"""Offline, exact-runtime Java 21 build of the standalone stone UI bridge; never installs."""
from pathlib import Path
import argparse, hashlib, json, os, subprocess, tempfile, zipfile

ROOT = Path(__file__).resolve().parent
BUILD = ROOT/'build'

def sha(path): return hashlib.sha256(path.read_bytes()).hexdigest()

def dependencies(prism):
    LIB = prism/'libraries'
    required = [
        LIB/'net/neoforged/neoforge/21.1.262/neoforge-21.1.262-client.jar',
        LIB/'net/minecraft/client/1.21.1-20240808.144430/client-1.21.1-20240808.144430-srg.jar',
        LIB/'net/neoforged/neoforge/21.1.262/neoforge-21.1.262-universal.jar',
        LIB/'net/neoforged/fancymodloader/loader/4.0.45/loader-4.0.45.jar',
        LIB/'net/fabricmc/sponge-mixin/0.16.4+mixin.0.8.7/sponge-mixin-0.16.4+mixin.0.8.7.jar',
    ]
    for path in required: assert path.is_file(), f'Missing runtime dependency: {path}'
    support = [p for p in LIB.rglob('*.jar') if p not in required and
               not p.name.startswith(('neoforge-', 'client-', 'forge-')) and '/net/minecraft/' not in p.as_posix()]
    return required, sorted(support)

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--prism-root', type=Path, default=os.environ.get('SB4_PRISM_ROOT'), help='PrismLauncher data directory containing libraries/')
    parser.add_argument('--jdk', type=Path, default=os.environ.get('SB4_JDK'), help='Java 21 JDK directory containing bin/javac')
    options = parser.parse_args()
    if options.prism_root is None or options.jdk is None:
        parser.error('Provide --prism-root and --jdk, or SB4_PRISM_ROOT and SB4_JDK.')
    javac = options.jdk/('bin/javac.exe' if os.name == 'nt' else 'bin/javac')
    if not javac.is_file():
        parser.error(f'JDK compiler not found: {javac}')
    BUILD.mkdir(exist_ok=True)
    required, support = dependencies(options.prism_root)
    with tempfile.TemporaryDirectory(prefix='classes-', dir=BUILD) as temporary:
        compile_and_package(javac, Path(temporary), required, support)

def compile_and_package(javac, CLASSES, required, support):
    classpath = os.pathsep.join(map(str, required+support))
    sources = sorted((ROOT/'src/main/java').rglob('*.java'))
    args = ['--release','21','-encoding','UTF-8','-proc:none','-classpath',classpath,'-d',str(CLASSES),*map(str,sources)]
    argfile = BUILD/'javac.args'
    argfile.write_text('\n'.join('"'+a.replace('\\','/').replace('"','\\"')+'"' for a in args),'utf-8')
    subprocess.run([str(javac),'@'+str(argfile)],check=True)
    artifact=BUILD/'sb4-stone-ui-bridge.jar'
    with zipfile.ZipFile(artifact,'w',compression=zipfile.ZIP_DEFLATED) as jar:
        jar.writestr('META-INF/MANIFEST.MF','Manifest-Version: 1.0\nImplementation-Version: 1.0.0\n\n')
        for root in [CLASSES,ROOT/'src/main/resources']:
            for p in sorted(root.rglob('*')):
                if p.is_file(): jar.write(p,p.relative_to(root).as_posix())
    report={'status':'pass','errors':[],'artifact':str(artifact),'sha256':sha(artifact),'java_release':21,
            'source_files':{p.relative_to(ROOT).as_posix():sha(p) for p in sources},
            'resource_files':{p.relative_to(ROOT).as_posix():sha(p) for p in sorted((ROOT/'src/main/resources').rglob('*')) if p.is_file()},
            'class_files':{p.relative_to(CLASSES).as_posix():sha(p) for p in sorted(CLASSES.rglob('*.class'))},
            'required_compile_dependencies':{str(p):sha(p) for p in required},
            'builder_sha256':sha(Path(__file__)),'instance_modified':False,'runtime_validation_pending':True}
    (BUILD/'build-report.json').write_text(json.dumps(report,ensure_ascii=False,indent=2)+'\n','utf-8')
    print(json.dumps({'artifact':str(artifact),'sha256':report['sha256'],'classes':len(list(CLASSES.rglob('*.class')))}))

if __name__=='__main__':main()
