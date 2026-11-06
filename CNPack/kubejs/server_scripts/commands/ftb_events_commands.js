//priority: 99
// Localized display labels only; persisted statistics keys remain unchanged.
const SB4_EVENT_LABELS_ZH = {
  "Chicken Jockey Confetti": "鸡骑士羽毛礼花",
  "Creeper": "苦力怕来袭",
  "Ceiling Collapse": "洞顶塌方",
  "Loot Bee Event": "寻宝蜜蜂",
  "Gift of Cats & Dogs!": "天降猫狗",
  "Stick Event": "奇趣木棍",
  "Questing Ram": "谜题羊",
  "Goat": "量子山羊",
  "Wandering Trader": "流浪商人",
  "Template": "事件模板",
  "Event System Error": "事件系统出错",
  "Player is creative or spectator": "玩家处于创造或旁观模式",
  "No Timer": "计时器未初始化",
  "Player Dead": "玩家已死亡",
  "On Cooldown": "冷却尚未结束",
  "Missing Stage": "尚未解锁所需阶段",
  "Player not in Team Dimension": "玩家不在团队基地维度",
  "Required Biome": "不在所需生物群系",
  "No Event": "没有可触发的事件",
  "Spawn not found": "找不到生成位置",
  "Ceiling Collapse - Insufficient locations": "洞顶塌方：可用位置不足",
  "Questing Ram - Already completed": "谜题羊：已完成挑战",
  "Questing Ram - Not in base dimension": "谜题羊：不在基地维度",
  "Questing Ram - Already exists": "谜题羊：已生成",
  "Questing Ram - Error": "谜题羊：发生错误"
};
const sb4EventLabel = (name) => SB4_EVENT_LABELS_ZH[String(name)] || String(name);
const setupCommand = (e, ctx) => {
  var source = ctx.getSource();
  if (!source) console.log("invalid source");

  var player = source.getPlayerOrException();

  if (!player) console.log("invalid player");

  if (!player.persistentData) console.log("invalid persistent data");

  var pData = player.persistentData;
  var disabledEvents = pData.disabledEvents;
  console.log(pData.disabledEvents);
  if (e.disableStage && !player.stages.has(e.disableStage)) {
    source.sendSuccess(Text.translate("ftb.event_system.commands.disablefail").red(), false);
    return 0;
  }
  if (disabledEvents) {
    let disabledEventsList = [];
    for (let i = 0; i < disabledEvents.length; i++) {
      disabledEventsList.push(disabledEvents[i].getAsString());
    }

    if (disabledEventsList.includes(e.name.toString())) {
      pData.disabledEvents = disabledEventsList.filter((item) => item !== e.name);
      source.sendSuccess(Text.translate("ftb.event_system.commands.toggle.on", [sb4EventLabel(e.displayName)]).green(), false);
      return 1;
    } else {
      pData.disabledEvents.push(e.name);
      source.sendSuccess(Text.translate("ftb.event_system.commands.toggle.off", [sb4EventLabel(e.displayName)]).green(), false);
      return 1;
    }
  } else {
    pData.disabledEvents = [e.name];
    source.sendSuccess(Text.translate("ftb.event_system.commands.toggle.off", [sb4EventLabel(e.displayName)]).green(), false);
    return 1;
  }
}

const getSuggestionsForEvent = (eventObj) => {
    return (ctx, builder) => {
            const eventSuggestions = eventObj.commandSuggestions || [];
            const suggestions = [];
            eventSuggestions.forEach(suggestion => {
                suggestions.push(JSON.stringify(suggestion));
            });

            if (suggestions.length != 0) {
                suggestions.forEach(suggestion => {
                  builder.suggest(suggestion);
              });
            }
            return builder.buildFuture();
        }
}

const registerForceSubcommands = (event, eventOrString) => {
  const { commands: Commands, arguments: Arguments, builtinSuggestions: Suggestions } = event;
  let name, eventObj;

  if (typeof eventOrString === "string") {
    name = eventOrString;
    eventObj = null;
  } else {
    name = eventOrString.name;
    eventObj = eventOrString;
  }


  return Commands.literal(name)
    .executes(function (ctx) { return executeEvent(ctx, null, eventObj, null); })
    .then(Commands.argument('target', Arguments.PLAYER.create(event))
      .executes(function (ctx) { return executeEvent(ctx, Arguments.PLAYER.getResult(ctx, "target"), eventObj, null); })

      .then(Commands.argument("viewername", Arguments.STRING.create(event))
        .executes(function (ctx) { return executeEvent(ctx, Arguments.PLAYER.getResult(ctx, "target"), eventObj, Arguments.STRING.getResult(ctx, 'viewername')); })

        .then(Commands.argument("options", Arguments.NBT_TAG.create(event))
            .suggests(getSuggestionsForEvent(eventObj))
            .executes(function (ctx) { return executeEvent(ctx, Arguments.PLAYER.getResult(ctx, "target"), eventObj, Arguments.STRING.getResult(ctx, 'viewername'), Arguments.NBT_TAG.getResult(ctx, "options")); })
        )
      )
      .then(Commands.argument("options", Arguments.NBT_TAG.create(event))
        .suggests(getSuggestionsForEvent(eventObj))
        .executes(function (ctx) { return executeEvent(ctx, Arguments.PLAYER.getResult(ctx, "target"), eventObj, null, Arguments.NBT_TAG.getResult(ctx, "options")); })
      )
    );
}

const executeEvent = (ctx, target, event, viewerName, options) => {


    var source = ctx.getSource();

    if (!source) console.log("invalid source");
    target = target ?? source.getPlayerOrException();
    new Statistics(target.getServer());

    let chosenEvent = event ?? checks.event.getRandomEvent(ftbEvents);
    let locationInfo = null;
    if (chosenEvent.size >= 0) {
      locationInfo = checks.event.findSpawnLocation(chosenEvent, target, 500);
      if(!locationInfo) {
        source.sendSuccess(Text.translate("ftb.event_system.spawn.not_found", [sb4EventLabel(chosenEvent.displayName)]).red(), false);
        return 0;
      }
    }
    options = options ?? {};
    chosenEvent.execute(ctx, target, locationInfo, viewerName, options);
    source.sendSuccess(Text.translate("ftb.event_system.commands.force.success", [sb4EventLabel(chosenEvent.displayName), target.username]).green(), false);
    Statistics().addSuccess(chosenEvent.displayName);
    // Statistics.success[chosenEvent.name] = Statistics.success[chosenEvent.name] ? Statistics.success[chosenEvent.name].getAsInt() + 1 : 1;
    return 1;
}


ServerEvents.commandRegistry((event) => {

  const { commands: Commands, arguments: Arguments, builtinSuggestions: Suggestions } = event;
  const EventSettingsArgs = {
    'timer': Arguments.INTEGER,
    'timerDelay': Arguments.INTEGER,
    'debug': Arguments.BOOLEAN,
    'maxAttempts': Arguments.INTEGER,
    'timeCooldown': Arguments.INTEGER,
  }

    event.register(Commands.literal("events")
        .then(Commands.literal('force').requires(src => src.hasPermission(3))
            .then(registerForceSubcommands(event, "ftb:random"))
        )
        .then(Commands.literal("settings").requires(src => src.hasPermission(3))
            .then(Commands.literal("list")
              .executes(function (ctx) {
                const source = ctx.getSource();
                let player = source.getPlayerOrException();
                let server = source.getServer();

                let sPData = server.getPersistentData();
                let eventSettings = sPData.eventSettings;
                let text = [];
                Object.keys(eventSettings).forEach(function (key) {
                  text.push(key + ": " + eventSettings[key]);
                })
                source.sendSuccess(Text.of([text.join("\n")]), false);
                return 1;
              })
            )
        )
        .then(Commands.literal("statistics")
          .requires(src => src.hasPermission(3))
          .executes(function (ctx) {
            const source = ctx.getSource();
            const server = source.getServer();
            new Statistics(server);
            let statistics = Statistics().getAsJson();

            if (!statistics) {
              source.sendSuccess(Text.translate("ftb.event_system.commands.statistics.no_data").red(), false);
              return 0;
            }

            let text = []
            text.push("事件触发总数：" + (Statistics().getTotalEvents() || 0));
            text.push("==============================")
            Object.keys(statistics.failure).forEach(function (key) {
              text.push(sb4EventLabel(key) + "，失败次数：" + statistics.failure[key]);
            })
            text.push("==============================")
            Object.keys(statistics.success).forEach(function (key) {
              text.push(sb4EventLabel(key) + "，成功次数：" + statistics.success[key]);
            })
            source.sendSuccess(Text.of([text.join("\n")]), false);
            return 1;
          })
          .then(Commands.literal("reset")
            .requires(src => src.hasPermission(3))
            .executes(function (ctx) {
              const source = ctx.getSource();
              Statistics().reset();
              source.sendSuccess(Text.translate("ftb.event_system.commands.statistics.reset").green(), false);
              return 1;
            })
        )
        )
    )

  // Registering the main Event commands
  ftbEvents.forEach(ftbEvent => {
    event.register(
      Commands.literal("events")
        .then(Commands.literal("toggle")
          .then(Commands.literal(ftbEvent.name)
            .executes(function (ctx) {
              setupCommand(ftbEvent, ctx);
              return 1;
            })
          )
        )

        .then(Commands.literal('force').requires(src => src.hasPermission(3))
          .then(registerForceSubcommands(event, ftbEvent))
        )
    );
  })

  // Event Settings Commands
  Object.keys(EventSettingsArgs).forEach(function (key) {
    event.register(Commands.literal("events")
        .then(Commands.literal("settings")
            .then(Commands.literal("set").requires(src => src.hasPermission(3))
            .then(Commands.literal(key)
                .then(Commands.argument(key, EventSettingsArgs[key].create(event))
                .executes(function (ctx) {
                    const source = ctx.getSource();
                    let server = source.getServer();
                    let sPData = server.getPersistentData();
                    let eventSettings = sPData.eventSettings;
                    let argument = EventSettingsArgs[key].getResult(ctx, key);
                    eventSettings[key] = argument;
                    source.sendSuccess(Text.translate("ftb.event_system.commands.settings.set", [key, argument]).green(), false);
                    return 1;
                })
                )
            )
            )
        )
    )
  })

});
