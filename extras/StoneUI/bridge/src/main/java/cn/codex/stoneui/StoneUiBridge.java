package cn.codex.stoneui;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

@Mod(value = "sb4_stone_ui_bridge", dist = Dist.CLIENT)
public final class StoneUiBridge {
    public StoneUiBridge(IEventBus modBus) {
        modBus.addListener(this::registerReloadListeners);
        NeoForge.EVENT_BUS.addListener(StoneUiMenus::registerCommands);
    }

    private void registerReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((ResourceManagerReloadListener) StoneUiTheme::reload);
    }
}
