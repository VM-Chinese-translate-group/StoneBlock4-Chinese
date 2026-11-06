package cn.codex.stoneui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

/** Only exact Minecraft options classes; mod subclasses and custom menus retain their own theme. */
public final class StoneUiScreens {
    public static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath("stoneblock4_stoneui", "textures/gui/settings_background.png");


    private StoneUiScreens() {}

    public static boolean draw(Screen screen, GuiGraphics graphics) {
        if (!StoneUiTheme.backgroundEnabled() || !VanillaOptionsScope.includes(screen.getClass().getName())) return false;
        StoneUiTheme.logHit("minecraft.options.background");
        // Single 2048x1152 workshop scene: cover the viewport, crop centrally, never stretch or tile.
        double ratio=(double)Math.max(1,screen.width)/Math.max(1,screen.height);
        int cropWidth=Math.max(1,Math.min(2048,(int)Math.round(1152*ratio)));
        int cropHeight=Math.max(1,Math.min(1152,(int)Math.round(2048/ratio)));
        graphics.blit(BACKGROUND, 0, 0, screen.width, screen.height,
            (2048-cropWidth)/2.0F, (1152-cropHeight)/2.0F, cropWidth, cropHeight, 2048, 1152);
        graphics.fill(0, 0, screen.width, screen.height, 0x10000000);
        return true;
    }
}
