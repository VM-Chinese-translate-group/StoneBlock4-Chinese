package cn.codex.stoneui;

import com.google.gson.GsonBuilder;
import com.mojang.brigadier.arguments.StringArgumentType;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

/** Read-only menu discovery; only an explicit command writes the diagnostic JSON. */
public final class StoneUiMenus {
    private StoneUiMenus() {}

    public static void registerCommands(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("stoneui")
            .then(Commands.literal("menus").executes(context -> catalog(context.getSource())))
            .then(Commands.literal("open").then(Commands.argument("modid", StringArgumentType.word())
                .suggests((context, builder) -> SharedSuggestionProvider.suggest(menuIds(), builder))
                .executes(context -> open(context.getSource(), StringArgumentType.getString(context, "modid"))))));
    }

    private static List<String> menuIds() {
        return ModList.get().getMods().stream().filter(info -> IConfigScreenFactory.getForMod(info).isPresent())
            .map(info -> info.getModId()).sorted().toList();
    }

    private static int catalog(CommandSourceStack source) {
        List<Map<String, Object>> menus = new ArrayList<>();
        ModList.get().getMods().stream().sorted(Comparator.comparing(info -> info.getModId())).forEach(info -> {
            IConfigScreenFactory.getForMod(info).ifPresent(factory -> {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("modid", info.getModId());
                row.put("display_name", info.getDisplayName());
                row.put("version", info.getVersion().toString());
                row.put("factory_class", factory.getClass().getName());
                row.put("command", "/stoneui open " + info.getModId());
                menus.add(row);
            });
        });
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("schema", 1);
        report.put("theme_enabled", StoneUiTheme.enabled());
        report.put("menu_count", menus.size());
        report.put("menus", menus);
        report.put("scope", "Registered official IConfigScreenFactory instances only; discovery does not open screens or modify settings. Other in-world menus are covered by the separate static matrix.");
        try {
            Path path = Minecraft.getInstance().gameDirectory.toPath().resolve("logs/stone-ui-menu-catalog.json");
            Files.createDirectories(path.getParent());
            Files.writeString(path, new GsonBuilder().setPrettyPrinting().create().toJson(report) + "\n", StandardCharsets.UTF_8);
            source.sendSuccess(() -> Component.literal("已记录 " + menus.size() + " 个官方配置菜单：logs/stone-ui-menu-catalog.json"), false);
            StoneUiTheme.LOGGER.info("[SB4 StoneUI] Menu catalog: {} registered config factories, {}", menus.size(), path);
            return menus.size();
        } catch (java.io.IOException error) {
            StoneUiTheme.LOGGER.error("[SB4 StoneUI] Could not write menu catalog", error);
            source.sendFailure(Component.literal("无法写入菜单诊断日志，请查看客户端日志。"));
            return 0;
        }
    }

    private static int open(CommandSourceStack source, String modid) {
        var container = ModList.get().getModContainerById(modid);
        if (container.isEmpty()) {
            source.sendFailure(Component.literal("未安装模组：" + modid));
            return 0;
        }
        var factory = IConfigScreenFactory.getForMod(container.get().getModInfo());
        if (factory.isEmpty()) {
            source.sendFailure(Component.literal("该模组未注册官方配置菜单：" + modid));
            return 0;
        }
        Minecraft minecraft = Minecraft.getInstance();
        // Queue after chat closes; execute() could run immediately and let ChatScreen replace the new screen.
        minecraft.tell(() -> {
            try {
                Screen parent = minecraft.screen;
                Screen screen = factory.get().createScreen(container.get(), parent);
                if (screen == null) throw new IllegalStateException("Config factory returned null: " + modid);
                minecraft.setScreen(screen);
                StoneUiTheme.LOGGER.info("[SB4 StoneUI] Opened official config menu: {} -> {}", modid, screen.getClass().getName());
            } catch (RuntimeException error) {
                StoneUiTheme.LOGGER.error("[SB4 StoneUI] Config factory failed for " + modid, error);
                source.sendFailure(Component.literal("配置菜单打开失败：" + modid + "，请查看日志。"));
            }
        });
        return 1;
    }
}
