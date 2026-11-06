package cn.codex.sb4compat;

import cn.codex.sb4compat.oracle.OracleLocalizedSearch;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

@Mod(value = "sb4_localization_compat", dist = Dist.CLIENT)
public final class Sb4LocalizationCompat {
    public Sb4LocalizationCompat(IEventBus modBus) {
        modBus.addListener(this::registerReloadListeners);
    }

    private void registerReloadListeners(RegisterClientReloadListenersEvent event) {
        if (ModList.get().isLoaded("oracle_index")) {
            event.registerReloadListener((ResourceManagerReloadListener) manager -> OracleLocalizedSearch.invalidate());
        }
    }
}
