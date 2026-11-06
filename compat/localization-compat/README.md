# 可选的客户端中文兼容组件

这部分配合 `CNPack` 中的译文，处理资源文件无法替换的硬编码文本和检索行为。它与石板界面主题无关，也不修改模组的按钮配色或设置背景。

- Oracle 1.4.0：在简体中文下检索中文标题、正文、英文别名和物品 ID，支持短中文查询；结果保持原页面 ID、解锁检查和打开入口。
- SimplyTooltips 0.1.5：在简体中文下替换录制通知和固定标签，保留动态错误详情、可点击文件夹与内部样式 ID。
- Patchouli 1.21.1-93-NEOFORGE：正整数版号显示为“第1版”等中文格式，自定义版号及其他语言仍走原逻辑。

组件曾在 Minecraft 1.21.1、NeoForge 21.1.262 上实测中文／英文切换、资源重载、Oracle 搜索和页面跳转，以及 Patchouli 版号切换。上述目标模组为可选依赖；未安装时跳过对应 Mixin。验证不代表兼容任意后续模组版本。

为避免普通汉化更新额外要求升级加载器，本提交将组件和源码放在此处，**没有默认加入 `CNPack/mods`**。维护者可独立评估；符合版本要求的玩家也可在退出游戏后把本目录的 `sb4-localization-compat.jar` 放入实例 `mods`。移除该 JAR 可撤回组件行为，不移除译文。不要用它替换原 Oracle、SimplyTooltips 或 Patchouli JAR。

`reviewed-artifact.json` 记录已测 JAR 以及全部 Java 源码、资源的 SHA-256。该 JAR 包含 19 个类、7 个 Mixin，每种语言 32 个语言键；源码与已测版本相同。

## 本地构建

需要 Python 3、Java 21 JDK，以及已经启动过的对应 Prism 实例和运行时依赖。构建不下载依赖、不安装模组、不访问存档。

```powershell
$env:SB4_PRISM_ROOT = '<Prism 数据目录>'
$env:SB4_INSTANCE = '<实例的 minecraft 目录>'
$env:SB4_JDK = '<Java 21 JDK 目录>'
python build.py
```

产物写入 `build/sb4-localization-compat.jar`。构建报告位于 `build/build-report.json`；本机依赖路径只写入被 Git 忽略的 `build/`。构建脚本按已验收的 NeoForge 21.1.262 / Oracle 1.4.0 依赖定位，缺失时会停止，不会擅自改换版本。
