// Add more streamers here
// trophymanager:trophy[custom_data={BaseBlock:"air",Name:"Super Hoe 纪念奖杯",OffsetY:1.0d,Scale:2.0d,TrophyItem:{id:"avaritia:infinity_catalyst"},TrophyType:"item"}]
//   'trophymanager:trophy[custom_data={BaseBlock:"air",Name:"Snock 纪念奖杯!",OffsetY:0.25d,Scale:1.5d,TrophyEntity:{entityType:"minecraft:cat", variant:"minecraft:all_black"},TrophyType:"entity"}]'

const streamers = {
  "Matrixis": {
    items: [ 'trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"Mat 的香蕉",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"oritech:banana"},TrophyType:"item"}]'],

    lines: ["庆祝 StoneBlock 4 发布，送上一份小小谢礼。", "来自 FTB 团队的心意"],
  },
  "MCTCReese": {
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"MCTCReese 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"minecraft:cactus"},TrophyType:"item"}]'],
    lines: ["庆祝 StoneBlock 4 发布，送上一份小小谢礼。", "来自 FTB 团队的心意"],
  },
  "AlfredGG": {
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"AlfredGG 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"gag:no_solicitors"},TrophyType:"item"}]'],
    lines: ["这座专属奖杯，送给我们最爱的任务书设计师！", "FTB 团队献上心意，Tazz 的那份尤其浓！"],
  },
  "TheonlyTazz":{
    items: ['minecraft:debug_stick'],
    lines: ["笨蛋，写完脚本记得测试啊！"]
  },
  "ChosenArchitect":{
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"ChosenArchitect 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"ars_nouveau:creative_spell_book"},TrophyType:"item"}]'],
    lines: ["庆祝 StoneBlock 4 发布，送上一份小小谢礼。", "来自 FTB 团队的心意"],
  },
  "ectorvynk":{
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"ectorvynk 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"ftb:clapple"},TrophyType:"item"}]'],
    lines: ["庆祝 StoneBlock 4 发布，送上一份小小谢礼。", "来自 FTB 团队的心意"],
  },
  "Lashmak":{
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"Lashmak 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"chicken_roost:c_purple"},TrophyType:"item"}]'],
    lines: ["黑暗领主驾到！", "庆祝 StoneBlock 4 发布，送上一份小小谢礼。", "来自 FTB 团队的心意"],
  },
  "Threefold":{
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"Threefold 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"minecraft:music_disc_precipice"},TrophyType:"item"}]'],
    lines: ["难得见你从格雷科技的流水线里出来歇口气！", "来自 Tazz 的心意"],
  },
  "SystemCollapse":{
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"SystemCollapse 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"ae2:singularity"},TrophyType:"item"}]'],
    lines: ["庆祝 StoneBlock 4 发布，送上一份小小谢礼。", "来自 FTB 团队的心意"],
  },
  "Vlofast":{
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"Vlofast 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"minecraft:cauldron"},TrophyType:"item"}]'],
    lines: ["庆祝 StoneBlock 4 发布，送上一份小小谢礼。", "来自 FTB 团队的心意"],
  },
  "Joprebond":{
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"Joprebond 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"ars_nouveau:frostaya_pod"},TrophyType:"item"}]'],
    lines: ["庆祝 StoneBlock 4 发布，送上一份小小谢礼。", "来自 FTB 团队的心意"],
  },
  "reninsane":{
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"reninsane 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"minecraft:cake"},TrophyType:"item"}]'],
    lines: ["庆祝 StoneBlock 4 发布，送上一份小小谢礼。", "来自 FTB 团队的心意"],
  },
  "MrButton_": {
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"MrButton_ 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"oritech:tech_button"},TrophyType:"item"}]'],
    lines: ["专门送你一枚特别的按钮——好吧，其实是奖杯！", "来自 FTB 团队的心意"],
  },
  "generikb": {
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"generikb 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"farmersdelight:dumplings"},TrophyType:"item"}]'],
    lines: ["庆祝 StoneBlock 4 发布，送上一份小小谢礼。", "来自 Tazz 的心意"],
  },
  "direwolf20":{
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"direwolf20 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"justdirethings:celestigem_paxel"},TrophyType:"item"}]'],
    lines: ["庆祝 StoneBlock 4 发布，送上一份小小谢礼。", "来自 FTB 团队的心意"],
  },
  "shynieke":{
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"才不是奖杯呢 :copium:",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"supplementaries:statue"},TrophyType:"item"}]'],
    lines: ["都说了，这可不是奖杯！", "来自 Tazz 的心意"],
  },
  "darkphan":{
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"darkphan 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"actuallyadditions:engineers_goggles"},TrophyType:"item"}]'],
    lines: ["庆祝 StoneBlock 4 发布，送上一份小小谢礼。", "来自 FTB 团队的心意"],
  },
  "forestfaerie": {
    items: ['trophymanager:trophy[custom_data={BaseBlock:"air",Name:"Snock 纪念奖杯!",OffsetY:0.25d,Scale:1.5d,TrophyEntity:{entityType:"minecraft:cat", variant:"minecraft:all_black"},TrophyType:"entity"}]'],
    lines: ["亲爱的 Snock！", "感谢你游玩 StoneBlock 4！", "来自 Tazz 的心意 "],
  },
  "ellelapsus":{
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"别具一格的奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"bhc:red_heart"},TrophyType:"item"}]'],
    lines: ["专门送给你 :)", "满满的心意 <3"],
  },
  "MiyoshiEira":{
    items: ['trophymanager:trophy[custom_data={BaseBlock:"air",Name:"送给 Miyo！",OffsetY:0.25d,Scale:1.5d,TrophyEntity:{entityType:"minecraft:wolf"},TrophyType:"entity"}]'],
    lines: ["庆祝 StoneBlock 4 发布，送上一份小小谢礼。", "为长久的友谊干杯！"],
  },
  "AlpDerps":{
    items: ['minecraft:cake'],
    lines: ["来，吃块蛋糕！", "来自 Tazz 的心意"],
  },
  "Saereth":{
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"Saereth 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"minecraft:ender_eye"},TrophyType:"item"}]'],
    lines: ["庆祝 StoneBlock 4 发布，送上一份小小谢礼。", "来自 FTB 团队的心意"],
  },
  "slowpoke101": {
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"Slowpoke 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"minecraft:clay_ball"},TrophyType:"item"}]'],
    lines: ["没错，说的就是你，老家伙！", "你也有一座专属奖杯。", "来自整合包开发组的心意 :)"],
  },
  "Jake_Evans": {
    items: ['trophymanager:trophy[custom_data={BaseBlock:"air",Name:"Jake 专属奖杯！",OffsetY:0.25d,Scale:1.5d,TrophyEntity:{entityType:"minecraft:parrot"},TrophyType:"entity"}]'],
    lines: ["庆祝 StoneBlock 4 发布，送上一份小小谢礼。", "来自 FTB 团队的心意"],
  },
  "twsparklecat": {
    items: ['trophymanager:trophy[custom_data={BaseBlock:"air",Name:"Sparkle 纪念奖杯!",OffsetY:0.25d,Scale:1.5d,TrophyEntity:{entityType:"minecraft:cat", variant:"minecraft:persian"},TrophyType:"entity"}]'],
    lines: ["送给 Sparkle 的专属奖杯！", "来自 FTB 团队的心意"],
  },
  "kSunekaer": {
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"Sunekaer\'s 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"minecraft:cobblestone"},TrophyType:"item"}]'],
    lines: ["送给 KSunekaer 的专属奖杯！", "来自 FTB 团队的心意"],
  },
  "xBCrafted": {
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"xBCrafted 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"minecraft:diamond_sword"},TrophyType:"item"}]'],
    lines: ["庆祝 StoneBlock 4 发布，送给 xBCrafted 一座专属奖杯！", "来自 FTB 团队的心意"],
  },
  "Hypnotizd": {
    items: ['trophymanager:trophy[custom_data={BaseBlock:"ftbechoes:echo_projector",Name:"Hypnotizd 纪念奖杯",OffsetY:0.6d,Scale:1.0d,TrophyItem:{id:"minecraft:diamond_chestplate"},TrophyType:"item"}]'],
    lines: ["庆祝 StoneBlock 4 发布，送给 Hypnotizd 一座专属奖杯！", "来自 FTB 团队的心意"],
  }
}



PlayerEvents.loggedIn((event) => {
  const { player, server } = event;

    // Custom Streamer Trophy and Lines
  if(Object.keys(streamers).includes(player.username)){
    if(!player.stages.has("streamer")){
      player.stages.add("streamer");
      streamers[player.username].items.forEach(item => {
        player.give(item);
      });
      streamers[player.username].lines.forEach(line => {
        player.tell(`[§6Feed The Beast§r] ${line}`);
      });
    }
  }

});
