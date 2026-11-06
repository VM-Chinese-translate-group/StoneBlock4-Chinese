package cn.codex.sb4compat.mixin;

import cn.codex.sb4compat.oracle.OracleLocalizedSearch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Pseudo
@Mixin(targets = "rearth.oracle.ui.SearchScreen", remap = false)
public abstract class OracleSearchScreenMixin {
    // Oracle 1.4.0 rejects queries of <=2 UTF-16 characters, which excludes common Chinese terms.
    @ModifyConstant(method = "onSearchTyped(Ljava/lang/String;)V", constant = @Constant(intValue = 2, ordinal = 0), require = 1)
    private int sb4$allowShortChineseQueries(int original) {
        return OracleLocalizedSearch.enabled() ? 0 : original;
    }
}
