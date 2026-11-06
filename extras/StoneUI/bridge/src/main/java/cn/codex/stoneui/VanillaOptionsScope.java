package cn.codex.stoneui;

import java.util.Set;

/** Exact vanilla names deliberately exclude every mod subclass and custom screen. */
public final class VanillaOptionsScope {
    private static final Set<String> VANILLA_OPTIONS = Set.of(
        "net.minecraft.client.gui.screens.options.OptionsScreen",
        "net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen",
        "net.minecraft.client.gui.screens.options.ChatOptionsScreen",
        "net.minecraft.client.gui.screens.options.FontOptionsScreen",
        "net.minecraft.client.gui.screens.options.LanguageSelectScreen",
        "net.minecraft.client.gui.screens.options.MouseSettingsScreen",
        "net.minecraft.client.gui.screens.options.OnlineOptionsScreen",
        "net.minecraft.client.gui.screens.options.SkinCustomizationScreen",
        "net.minecraft.client.gui.screens.options.SoundOptionsScreen",
        "net.minecraft.client.gui.screens.options.VideoSettingsScreen",
        "net.minecraft.client.gui.screens.options.controls.ControlsScreen",
        "net.minecraft.client.gui.screens.options.controls.KeyBindsScreen"
    );
    private VanillaOptionsScope() {}
    public static boolean includes(String className) { return VANILLA_OPTIONS.contains(className); }
}
