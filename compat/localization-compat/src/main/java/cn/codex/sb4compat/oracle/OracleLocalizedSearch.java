package cn.codex.sb4compat.oracle;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rearth.oracle.OracleClient;
import rearth.oracle.SemanticSearch;
import rearth.oracle.docs.DocsFormat;
import rearth.oracle.util.MarkdownParser;

public final class OracleLocalizedSearch {
    private static final Logger LOGGER = LoggerFactory.getLogger("SB4LocalizationCompat/Oracle");
    private static final ExecutorService WORKER = Executors.newSingleThreadExecutor(task -> {
        Thread thread = new Thread(task, "SB4-Chinese-Oracle-Index");
        thread.setDaemon(true);
        return thread;
    });
    private record Cache(String language, long generation, ResourceManager manager, CompletableFuture<KeywordIndex> index) {}
    private static long generation;
    private static Cache cache;

    private OracleLocalizedSearch() {}

    public static boolean enabled() {
        return OracleClient.getActiveLangCode().equals("zh_cn");
    }

    public static synchronized void invalidate() {
        generation++;
        cache = null;
    }

    private static synchronized Cache current() {
        String language = OracleClient.getActiveLangCode();
        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        if (cache == null || !cache.language.equals(language) || cache.generation != generation || cache.manager != manager) {
            // Capture the language and formats: a language change must never affect an in-flight index.
            Map<String, DocsFormat> formats = new HashMap<>(OracleClient.LOADED_WIKIS);
            Language translations = Language.getInstance();
            CompletableFuture<KeywordIndex> future = CompletableFuture.supplyAsync(() -> build(manager, language, formats, translations), WORKER);
            cache = new Cache(language, generation, manager, future);
        }
        return cache;
    }

    /** Null delegates to Oracle's original readiness/error handling if our index could not be built. */
    public static Boolean ready() {
        Cache selected = current();
        return selected.index.isCompletedExceptionally() ? null : selected.index.isDone();
    }

    /** SearchResult IDs use wiki:relative/page.mdx, exactly as Oracle's original search does. */
    public static ArrayList<SemanticSearch.SearchResult> search(String query) {
        Cache selected = current();
        if (selected.index.isCompletedExceptionally()) return null;
        KeywordIndex index;
        try {
            index = selected.index.getNow(null);
        } catch (CompletionException exception) {
            // Completion can fail between the readiness check and getNow during a reload.
            return null;
        }
        var results = new ArrayList<SemanticSearch.SearchResult>();
        if (index == null) return results;
        for (KeywordIndex.Hit hit : index.search(query, 24)) {
            KeywordIndex.Page page = hit.page();
            results.add(new SemanticSearch.SearchResult(List.of(hit.excerpt()), hit.score(), page.title(),
                    ResourceLocation.parse(page.id()), page.icon()));
        }
        return results;
    }

    private static KeywordIndex build(ResourceManager manager, String language, Map<String, DocsFormat> formats, Language translations) {
        long started = System.nanoTime();
        var pages = new ArrayList<KeywordIndex.Page>();
        int translated = 0;
        int skipped = 0;
        try {
            var resources = manager.listResources("books", id -> id.getPath().endsWith(".mdx"));
            for (var entry : resources.entrySet()) {
                ResourceLocation sourceId = entry.getKey();
                String[] path = sourceId.getPath().split("/", 3);
                if (path.length != 3) continue;
                String wiki = path[1];
                DocsFormat format = formats.get(wiki);
                if (format == null || format.isTranslatedPath(sourceId.getPath())) continue;
                try {
                    String english = read(entry.getValue());
                    String translatedPath = "books/" + wiki + format.getTranslatedDir(language) + "/" + path[2];
                    var localizedResource = manager.getResource(ResourceLocation.fromNamespaceAndPath(sourceId.getNamespace(), translatedPath));
                    String localized = english;
                    if (localizedResource.isPresent()) { localized = read(localizedResource.get()); translated++; }
                    var sourceMeta = MarkdownParser.parseFrontmatter(english);
                    var localMeta = MarkdownParser.parseFrontmatter(localized);
                    String fallback = path[2].replaceFirst("\\.mdx$", "");
                    String englishTitle = title(english, sourceMeta, fallback);
                    String icon = localMeta.getOrDefault("icon", sourceMeta.getOne("icon"));
                    if (icon == null || icon.isBlank()) icon = sourceMeta.getOne("id");
                    // SearchScreen parses icons as ResourceLocations; malformed metadata must not crash results.
                    if (icon != null && ResourceLocation.tryParse(icon) == null) icon = null;
                    String titleFallback = englishTitle;
                    if (icon != null) {
                        ResourceLocation itemId = ResourceLocation.parse(icon);
                        if (BuiltInRegistries.ITEM.containsKey(itemId)) {
                            String key = BuiltInRegistries.ITEM.get(itemId).getDescriptionId();
                            titleFallback = translations.getOrDefault(key, englishTitle);
                        }
                    }
                    String title = title(localized, localMeta, titleFallback);
                    pages.add(new KeywordIndex.Page(wiki + ":" + path[2], title, englishTitle,
                            KeywordIndex.plainText(localized), KeywordIndex.plainText(english), icon));
                } catch (Exception exception) {
                    skipped++;
                    LOGGER.debug("Skipped unreadable Oracle page {}", sourceId, exception);
                }
            }
            LOGGER.info("Chinese Oracle search index ready: {} pages ({} translated, {} skipped), language={}, {} ms",
                    pages.size(), translated, skipped, language, (System.nanoTime() - started) / 1_000_000);
            return new KeywordIndex(pages);
        } catch (RuntimeException exception) {
            LOGGER.warn("Chinese Oracle index failed; retaining Oracle's original search", exception);
            throw exception;
        }
    }

    private static String title(String text, MarkdownParser.Frontmatter metadata, String fallback) {
        String title = metadata.getOne("title");
        if (title == null || title.isBlank()) title = MarkdownParser.parseHeadingTitle(text);
        return title == null || title.isBlank() ? fallback : title;
    }

    private static String read(Resource resource) throws IOException {
        try (var input = resource.open()) { return new String(input.readAllBytes(), StandardCharsets.UTF_8); }
    }
}
