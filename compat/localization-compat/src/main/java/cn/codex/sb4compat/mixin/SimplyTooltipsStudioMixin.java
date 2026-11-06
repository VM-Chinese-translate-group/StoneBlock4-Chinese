package cn.codex.sb4compat.mixin;

import cn.codex.sb4compat.SimplyTooltipsText;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "net.sweenus.simplytooltips.client.studio.ThemeStudioScreen", remap = false)
public abstract class SimplyTooltipsStudioMixin {
    @Redirect(method = "buildInspector", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;"), require = 1)
    private MutableComponent sb4compat$translateInspectorLabels(String original) {
        // Only labels; the dropdown values still use the original motif/style IDs.
        return SimplyTooltipsText.component(original);
    }

    @ModifyConstant(method = {"buildStage", "drawStage"}, constant = {
        @Constant(stringValue = "BADGES"), @Constant(stringValue = "ITEM"), @Constant(stringValue = "user")
    }, require = 1)
    private String sb4compat$translateStageLabels(String original) {
        return SimplyTooltipsText.text(original);
    }

    @ModifyConstant(method = "drawZoomBadge", constant = @Constant(stringValue = "  dbl-click to fit"), require = 1)
    private String sb4compat$translateZoomHint(String original) {
        return SimplyTooltipsText.text(original);
    }
}
