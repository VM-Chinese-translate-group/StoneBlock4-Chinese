package cn.codex.sb4compat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/** Client-side adapter; every optional-mod hook remains scoped to a known display site. */
public final class SimplyTooltipsText {
    private SimplyTooltipsText() {}

    public static MutableComponent component(String original) {
        Minecraft minecraft = Minecraft.getInstance();
        String language = minecraft == null || minecraft.getLanguageManager() == null
            ? null : minecraft.getLanguageManager().getSelected();
        SimplyTooltipsTranslations.Translation match = SimplyTooltipsTranslations.match(original, language);
        if (match == null) return Component.literal(original);
        return match.argument() == null
            ? Component.translatableWithFallback(match.key(), original)
            : Component.translatableWithFallback(match.key(), original, match.argument());
    }

    public static String text(String original) {
        return component(original).getString();
    }
}
