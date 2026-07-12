---
navigation:
  title: 自动化配方
  position: 2
  icon: "oritechthings:particle_accelerator_speed_sensor"
  parent: oritech:getting_started.md
item_ids:
  - oritechthings:particle_accelerator_speed_sensor
  - oritech:accelerator_sensor
  - oritechthings:advanced_target_designator
---
# 自动化配方

<Column alignItems="center" fullWidth={true}>
### <ItemImage id="oritechthings:particle_accelerator_speed_sensor" scale="0.75" /> <Color id="aqua">粒子加速器自动化</Color> <ItemImage id="oritechthings:particle_accelerator_speed_sensor" scale="0.75" />
</Column>

---

<Color id="aqua">粒子加速器</Color>的配方只有在第二个物品与被加速的粒子以正确速度碰撞时才会完成。要实现自动化，需要两件事：

1. 检测粒子何时达到配方所需速度。
2. 在恰当时机释放碰撞物品。

<Color id="aqua">速度传感器</Color>负责第1步。<Color id="green">发射器</Color>+<Color id="green">漏斗</Color>组合负责第2步。

---

<Column alignItems="center" fullWidth={true}>
## <Color id="gold">速度传感器</Color>
</Column>

<Column alignItems="center" fullWidth={true}>
<ItemImage scale="3" id="oritechthings:particle_accelerator_speed_sensor" />
</Column>

<Color id="aqua">速度传感器</Color>会在链接的加速器内粒子达到目标速度时发出红石信号。它有两种模式：

- <Color id="green">自动</Color> — 直接从加速器当前激活的配方中读取所需速度。适用于普通配方。
- <Color id="yellow">手动</Color> — 由你自己设定阈值，适用于自定义红石逻辑。

GUI中的开/关切换决定传感器是否输出信号。

---

### <Color id="aqua">链接</Color>

链接方式与<Color id="gold">磁场</Color>相同，均使用<ItemLink id="oritechthings:advanced_target_designator" />：

1. 手持标识器对<Color id="aqua">粒子加速器</Color>潜行+右击，保存其位置。
2. 将<Color id="aqua">速度传感器</Color>放置在任意距离内的<Color id="green">128格</Color>范围内。
3. 对速度传感器潜行+右击标识器进行绑定。

绑定后，传感器的 GUI 会显示已链接的加速器坐标。

<RecipeFor id="oritechthings:particle_accelerator_speed_sensor" />

---

<Column alignItems="center" fullWidth={true}>
## <Color id="gold">投放碰撞物品</Color>
</Column>

直接将<Color id="green">发射器</Color>紧贴<Color id="aqua">粒子加速器</Color>放置并不会将物品直接推入加速器的库存。它会将物品以实体形式弹出。

解决方法：在发射器与加速器之间放置一只<Color id="green">漏斗</Color>。漏斗会接住掉落的物品实体，并将其正常输送到加速器内。

<Column alignItems="center" fullWidth={true}>
<Row>
  <ItemImage scale="2" id="minecraft:dispenser" />
  <ItemImage scale="2" id="minecraft:hopper" />
  <ItemImage scale="2" id="oritech:accelerator_controller" />
</Row>
</Column>

让发射器朝向漏斗，使掉落的物品实体落入漏斗，然后让漏斗朝向加速器。将碰撞物品填满发射器。

---

<Column alignItems="center" fullWidth={true}>
## <Color id="gold">整体搭建</Color>
</Column>

1. 搭建一个可用的加速器（参见 [基础知识](oritech:items/the_basics.md)）。
2. 放置并链接<Color id="aqua">速度传感器</Color>，保持其在<Color id="green">自动</Color>模式。
3. 将<Color id="green">发射器</Color>+<Color id="green">漏斗</Color>连接到加速器的一个输入面，并在发射器中放入碰撞物品。
4. 将速度传感器的红石输出接到发射器。
5. 将第一个原料放入加速器并为马达供电。系统将在粒子达到配方速度时自动发射第二个物品。

相比之下，基础的<ItemLink id="oritech:accelerator_sensor" />仅输出与粒子速度成比例的模拟信号——虽然可用，但需要比较器和逐个配方手动调节阈值。<Color id="aqua">速度传感器</Color>的自动模式可省去这一步。
