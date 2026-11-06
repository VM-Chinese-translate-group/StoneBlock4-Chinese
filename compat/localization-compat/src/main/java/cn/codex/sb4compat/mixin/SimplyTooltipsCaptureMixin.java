package cn.codex.sb4compat.mixin;

import cn.codex.sb4compat.SimplyTooltipsText;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "net.sweenus.simplytooltips.client.render.TooltipGifRecorder", remap = false)
public abstract class SimplyTooltipsCaptureMixin {
    @Redirect(
        method = {"requestCapture", "tick", "captureDueFrames", "finishRecording", "lambda$finishRecording$1"},
        at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;"),
        require = 1
    )
    private static MutableComponent sb4compat$translateCaptureMessage(String original) {
        // The path literal is unknown to the exact matcher and retains its original text.
        // The caller still appends it with its original click event, colour and underline.
        return SimplyTooltipsText.component(original);
    }
}
