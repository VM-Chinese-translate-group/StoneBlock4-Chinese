package cn.codex.sb4compat.mixin;

import cn.codex.sb4compat.oracle.OracleLocalizedSearch;
import java.util.ArrayList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rearth.oracle.SemanticSearch;

@Pseudo
@Mixin(targets = "rearth.oracle.SemanticSearch", remap = false)
public abstract class OracleSemanticSearchMixin {
    @Inject(method = "isReady()Z", at = @At("HEAD"), cancellable = true, require = 1)
    private void sb4$localizedReady(CallbackInfoReturnable<Boolean> cir) {
        if (OracleLocalizedSearch.enabled()) {
            Boolean ready = OracleLocalizedSearch.ready();
            if (ready != null) cir.setReturnValue(ready);
        }
    }

    @Inject(method = "search(Ljava/lang/String;)Ljava/util/ArrayList;", at = @At("HEAD"), cancellable = true, require = 1)
    private void sb4$localizedSearch(String query, CallbackInfoReturnable<ArrayList<SemanticSearch.SearchResult>> cir) {
        if (OracleLocalizedSearch.enabled()) {
            var results = OracleLocalizedSearch.search(query);
            if (results != null) cir.setReturnValue(results);
        }
    }
}
