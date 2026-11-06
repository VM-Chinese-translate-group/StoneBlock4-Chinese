package cn.codex.sb4compat.oracle;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

/** Small deterministic multilingual keyword index. No network, model, or Minecraft dependency. */
public final class KeywordIndex {
    public record Page(String id, String title, String englishTitle, String text, String englishText, String icon) {}
    public record Hit(Page page, double score, String excerpt) {}
    private record Indexed(Page page, String title, String englishTitle, String text, String englishText, String id) {}
    private static final Pattern HAN = Pattern.compile("[\\p{IsHan}]+");
    private static final Set<String> STOP_WORDS = Set.of("a", "an", "the", "of", "to", "how", "do", "i", "is", "and", "in", "with");
    private final List<Indexed> pages;

    public KeywordIndex(List<Page> source) {
        pages = source.stream().map(p -> new Indexed(p, normalize(p.title()), normalize(p.englishTitle()),
                normalize(p.text()), normalize(p.englishText()), normalize(p.id() + " " + Objects.toString(p.icon(), "")))).toList();
    }
    public int size() { return pages.size(); }

    public List<Hit> search(String rawQuery, int limit) {
        String query = normalize(rawQuery).trim();
        if (query.isBlank()) return List.of();
        Set<String> terms = terms(query);
        if (terms.isEmpty()) return List.of();
        var result = new ArrayList<Hit>();
        for (Indexed p : pages) {
            double score = 0;
            if (p.title.equals(query) || p.englishTitle.equals(query)) score += 1000;
            if (p.title.contains(query) || p.englishTitle.contains(query)) score += 250;
            if (p.text.contains(query)) score += 35;
            if (p.englishText.contains(query)) score += 15;
            if (p.id.contains(query)) score += 80;
            int matches = 0;
            for (String term : terms) {
                if (p.title.contains(term) || p.englishTitle.contains(term)) { score += 24; matches++; }
                else if (p.id.contains(term)) { score += 12; matches++; }
                else if (p.text.contains(term)) { score += 5; matches++; }
                else if (p.englishText.contains(term)) { score += 2; matches++; }
            }
            double coverage = (double) matches / terms.size();
            if (matches == 0 || coverage < 0.5) continue;
            score *= coverage;
            result.add(new Hit(p.page, score, excerpt(p.page.text(), query, terms)));
        }
        result.sort(Comparator.comparingDouble(Hit::score).reversed().thenComparing(h -> h.page.id()));
        return List.copyOf(result.subList(0, Math.min(limit, result.size())));
    }

    static String normalize(String text) {
        return Normalizer.normalize(Objects.toString(text, ""), Normalizer.Form.NFKC).toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");
    }

    private static Set<String> terms(String query) {
        var result = new LinkedHashSet<String>();
        for (String token : query.split("[^\\p{L}\\p{N}_:]+")) {
            if (token.isBlank() || STOP_WORDS.contains(token)) continue;
            var han = HAN.matcher(token);
            if (!han.find()) { result.add(token); continue; }
            han.reset();
            while (han.find()) {
                int[] characters = han.group().codePoints().toArray();
                if (characters.length == 1) result.add(new String(characters, 0, 1));
                for (int i = 0; i + 1 < characters.length; i++) result.add(new String(characters, i, 2));
            }
            for (String latin : token.split("[\\p{IsHan}]+")) if (!latin.isBlank()) result.add(latin);
        }
        return result;
    }

    private static String excerpt(String text, String query, Set<String> terms) {
        String plain = text.replaceAll("\\s+", " ").trim();
        String lower = normalize(plain);
        int position = lower.indexOf(query);
        if (position < 0) {
            for (String term : terms) {
                int found = lower.indexOf(term);
                if (found >= 0 && (position < 0 || found < position)) position = found;
            }
        }
        // Normalization may change length; clamp offsets back to the original safely.
        int start = Math.min(Math.max(0, position - 45), plain.length());
        if (start > 0 && start < plain.length() && Character.isLowSurrogate(plain.charAt(start))) start--;
        int end = Math.min(plain.length(), start + 220);
        if (end > start && end < plain.length() && Character.isHighSurrogate(plain.charAt(end - 1))) end--;
        return (start > 0 ? "…" : "") + plain.substring(start, end) + (end < plain.length() ? "…" : "");
    }

    public static String plainText(String markdown) {
        return markdown.replaceFirst("(?s)\\A\\s*---\\R.*?\\R---\\s*", "")
                .replaceAll("(?s)<!--.*?-->", " ")
                .replaceAll("(?s)```.*?```", " ")
                .replaceAll("!\\[[^]]*]\\([^)]*\\)", " ")
                .replaceAll("\\[([^]]+)]\\([^)]*\\)", "$1")
                .replaceAll("(?s)<[^>]*>", " ")
                .replaceAll("(?m)^[ \\t]*\\|?[ :\\-]+(?:\\|[ :\\-]+)+\\|?[ \\t]*$", " ")
                .replaceAll("(?m)^[ \\t]*(?:-{3,}|_{3,}|\\*{3,})[ \\t]*$", " ")
                .replaceAll("(?m)^[ \\t]*(?:>[ \\t]*)+", "")
                .replaceAll("(?m)^[ \\t]*\\|(.+)\\|[ \\t]*$", "$1")
                .replaceAll("[ \\t]*\\|[ \\t]*", " · ")
                .replaceAll("(?m)^\\s*(?:#{1,6}|[-*+])\\s+", "")
                .replace("**", "").replace("__", "").replace("`", "")
                .replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">")
                .replaceAll("\\s+", " ").trim();
    }
}
