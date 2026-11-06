package cn.codex.sb4compat;

import java.util.Map;

/** Exact, display-only rules. No Minecraft or optional-mod classes are linked here. */
public final class SimplyTooltipsTranslations {
    private static final String PREFIX = "sb4_localization_compat.simplytooltips.";
    private static final String SAVE_ERROR = "[Simply Tooltips] Failed to save tooltip GIF: ";
    private static final Map<String, String> KEYS = Map.ofEntries(
        Map.entry("[Simply Tooltips] GIF capture is already in progress.", "capture.busy"),
        Map.entry("[Simply Tooltips] No tooltip is currently visible to capture.", "capture.no_tooltip"),
        Map.entry("[Simply Tooltips] Recording tooltip GIF (6.0s @ 20 FPS)...", "capture.recording"),
        Map.entry("[Simply Tooltips] Capture cancelled: tooltip is no longer visible.", "capture.cancelled"),
        Map.entry("[Simply Tooltips] Failed to capture tooltip frame.", "capture.frame_failed"),
        Map.entry("[Simply Tooltips] GIF capture finished, but no frames were recorded.", "capture.no_frames"),
        Map.entry("[Simply Tooltips] Saved looping tooltip GIF. Folder: ", "capture.saved"),
        Map.entry("Slots", "tooltip.slots"),
        Map.entry("Slots  ", "tooltip.slots_spaced"),
        Map.entry("◆ Upgrades", "tooltip.upgrades"),
        Map.entry("Rune: ", "tooltip.rune_label"),
        Map.entry("◆ Rune", "tooltip.rune"),
        Map.entry("Description", "tooltip.description"),
        Map.entry("Implicit", "tooltip.implicit"),
        Map.entry("\ue000Description", "tooltip.description_marked"),
        Map.entry("Attack Damage", "tooltip.attack_damage"),
        Map.entry("Attack Speed", "tooltip.attack_speed"),
        Map.entry("Attack Range", "tooltip.attack_range"),
        Map.entry("motif", "studio.motif"),
        Map.entry("borderStyle", "studio.border_style"),
        Map.entry("itemAnimStyle", "studio.item_animation"),
        Map.entry("titleAnimStyle", "studio.title_animation"),
        Map.entry("itemBorderShape", "studio.item_border_shape"),
        Map.entry("textShadow", "studio.text_shadow"),
        Map.entry("BADGES", "studio.badges"),
        Map.entry("ITEM", "studio.item"),
        Map.entry("user", "studio.user_theme"),
        Map.entry("  dbl-click to fit", "studio.double_click_fit"),
        Map.entry("Themes", "studio.themes"),
        Map.entry("no matches", "studio.no_matches")
    );

    private SimplyTooltipsTranslations() {}

    /** Null means retain the original text, including for every non-zh_cn language. */
    public static Translation match(String original, String language) {
        if (!"zh_cn".equals(language) || original == null) return null;
        String key = KEYS.get(original);
        if (key != null) return new Translation(PREFIX + key, null);
        if (original.startsWith(SAVE_ERROR)) {
            // Keep the actual diagnostic unchanged; do not translate paths or exception text.
            return new Translation(PREFIX + "capture.save_failed", original.substring(SAVE_ERROR.length()));
        }
        return null;
    }

    public record Translation(String key, String argument) {}
}
