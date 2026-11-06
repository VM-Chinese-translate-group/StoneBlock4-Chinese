package cn.codex.sb4compat;

/** Language gate for the one Patchouli edition display branch that uses English ordinals. */
public final class PatchouliEdition {
    private PatchouliEdition() {}

    /** Zero means leave the original method untouched, including custom and developer editions. */
    public static int positiveVersion(String language, String version) {
        if (!"zh_cn".equals(language)) return 0;
        try {
            int number = Integer.parseInt(version);
            return number > 0 ? number : 0;
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }
}
