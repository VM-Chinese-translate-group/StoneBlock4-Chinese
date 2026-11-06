package cn.codex.sb4compat.mixin;

import cn.codex.sb4compat.SimplyTooltipsText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Pseudo
@Mixin(targets = "net.sweenus.simplytooltips.client.studio.widget.ThemeListWidget", remap = false)
public abstract class SimplyTooltipsThemeListMixin {
    @ModifyConstant(method = "<init>", constant = @Constant(stringValue = "Themes"), require = 1)
    private static String sb4compat$translateListTitle(String original) {
        return SimplyTooltipsText.text(original);
    }

    @ModifyConstant(method = "renderWidget", constant = @Constant(stringValue = "no matches"), require = 1)
    private String sb4compat$translateEmptyResult(String original) {
        return SimplyTooltipsText.text(original);
    }
}
