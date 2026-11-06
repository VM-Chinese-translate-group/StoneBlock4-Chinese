package cn.codex.sb4compat.mixin;

import cn.codex.sb4compat.PatchouliEdition;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "vazkii.patchouli.common.book.Book", remap = false)
public abstract class PatchouliBookEditionMixin {
    @Shadow @Final public String version;

    @Inject(method = "getSubtitle()Lnet/minecraft/network/chat/MutableComponent;",
        at = @At("HEAD"), cancellable = true, require = 1)
    private void sb4$chineseEdition(CallbackInfoReturnable<MutableComponent> cir) {
        Minecraft minecraft = Minecraft.getInstance();
        String language = minecraft == null || minecraft.getLanguageManager() == null
            ? null : minecraft.getLanguageManager().getSelected();
        int number = PatchouliEdition.positiveVersion(language, version);
        if (number > 0) {
            cir.setReturnValue(Component.translatable(
                "sb4_localization_compat.patchouli.edition", number));
        }
    }
}
