package cn.codex.stoneui;

import com.mojang.logging.LogUtils;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;

/** Resource-pack lifecycle only; no mod UI rendering, buttons, foreground or config changes. */
public final class StoneUiTheme {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceLocation MARKER = ResourceLocation.fromNamespaceAndPath("stoneblock4_stoneui", "theme.json");
    private static final Set<String> HITS = ConcurrentHashMap.newKeySet();
    private static volatile boolean enabled;
    private static volatile boolean backgroundAvailable;

    private StoneUiTheme() {}

    public static void reload(ResourceManager manager) {
        enabled = manager.getResource(MARKER).isPresent();
        backgroundAvailable = manager.getResource(StoneUiScreens.BACKGROUND).isPresent();
        HITS.clear();
        LOGGER.info("[SB4 StoneUI] Resource reload: enabled={}, backgroundAvailable={}, marker={}", enabled, backgroundAvailable, MARKER);
    }

    public static boolean enabled() { return enabled; }
    public static boolean backgroundEnabled() { return enabled && backgroundAvailable; }
    public static void logHit(String hook) {
        if (HITS.add(hook)) LOGGER.info("[SB4 StoneUI] First background hook: {}", hook);
    }
}
