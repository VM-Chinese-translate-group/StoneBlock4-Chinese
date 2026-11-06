package cn.codex.stoneui.mixin;

import cn.codex.stoneui.StoneUiScreens;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value=Screen.class, remap=false, priority=2000)
public abstract class ScreenBackgroundMixin {
    @Inject(method="renderBackground(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at=@At("HEAD"), cancellable=true, require=1)
    private void stoneui$vanillaOptionsBackground(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (StoneUiScreens.draw((Screen)(Object)this, graphics)) ci.cancel();
    }
}
