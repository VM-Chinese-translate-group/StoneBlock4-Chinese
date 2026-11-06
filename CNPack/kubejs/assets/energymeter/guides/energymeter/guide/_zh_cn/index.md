---
navigation:
  title: 电能表
  icon: meter
item_ids:
  - meter
---

# 电能表

欢迎阅读**电能表（Energy Meter）**模组指南！

<br clear="all" />

![](assets/preview.png)

<FloatingImage src="assets/logo.png" align="right"/>

这是一个小巧的内容模组，加入了用来测量能量传输速率的 <ItemLink id="meter" components="rarity=epic"/>。它的工作方式类似线缆：将输入的能量请求转交给输出端，同时记录传输量，再按设定的采样间隔计算速率。

除了完整的操作界面，电能表正面还有数字显示屏，随时都能直接读出当前速率。显示屏可朝向任意方向，包括朝上或朝下。由于正面用于显示，只有其余五个面可以设为输入或输出。

界面中的图表会显示最近 10 个采样间隔的读数，并支持暂停显示，方便回看数据。你还可以调整测量方式、采样间隔和计算模式等设置。<br/>
详细说明见[操作界面](interface.md)一页。

<br clear="all" />
<RecipeFor id="meter"/>
