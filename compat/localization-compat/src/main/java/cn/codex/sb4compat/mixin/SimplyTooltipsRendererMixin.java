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
@Mixin(targets = "net.sweenus.simplytooltips.client.render.TooltipRenderer", remap = false)
public abstract class SimplyTooltipsRendererMixin {
    @ModifyConstant(method = "render", constant = {
        @Constant(stringValue = "Slots"),
        @Constant(stringValue = "Slots  "),
        @Constant(stringValue = "◆ Upgrades"),
        @Constant(stringValue = "Rune: "),
        @Constant(stringValue = "◆ Rune")
    }, require = 1)
    private static String sb4compat$translateSectionLabels(String original) {
        // These constants only label/measure UI text; matching and serialized IDs are untouched.
        return SimplyTooltipsText.text(original);
    }

    @ModifyConstant(method = "prepareAbilitySection", constant = {
        @Constant(stringValue = "Description"), @Constant(stringValue = "Implicit"),
        @Constant(stringValue = "\ue000Description")
    }, require = 1)
    private static String sb4compat$translatePreparedSectionTitles(String original) {
        // Only output titles in this method; startsWithImplicitSection and regexes stay original.
        return SimplyTooltipsText.text(original);
    }

    @Redirect(method = "drawInlineStatRow", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;"), require = 1)
    private static MutableComponent sb4compat$translateDrawnStatLabels(String original) {
        // Localize at drawing time so the original row labels still drive statRowHeight correctly.
        return SimplyTooltipsText.component(original);
    }
}
