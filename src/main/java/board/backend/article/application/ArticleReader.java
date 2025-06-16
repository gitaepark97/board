package board.backend.article.application;

import board.backend.article.application.port.ArticleRepository;
import board.backend.article.domain.Article;
import board.backend.article.domain.ArticleNotFound;
import board.backend.common.event.ArticleReadEvent;
import board.backend.common.infra.CachedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@NamedInterface
@RequiredArgsConstructor
@Component
public class ArticleReader {

    private static final Duration CACHE_TTL = Duration.ofMinutes(5);

    private final CachedRepository<Article, Long> cachedArticleRepository;
    private final ArticleRepository articleRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void checkArticleExistsOrThrow(Long articleId) {
        if (cachedArticleRepository.findByKey(articleId).isEmpty() && !articleRepository.existsById(articleId)) {
            throw new ArticleNotFound();
        }
    }

    public List<Article> readAll(Long boardId, Long pageSize, Long lastArticleId) {
        return lastArticleId == null ?
            articleRepository.findAllByBoardId(boardId, pageSize) :
            articleRepository.findAllByBoardId(boardId, pageSize, lastArticleId);
    }

    public List<Article> readAll(List<Long> articleIds) {
        // 캐시 조회
        List<Article> articles = cachedArticleRepository.finalAllByKey(articleIds);

        Map<Long, Article> map = articles.stream()
            .collect(Collectors.toMap(Article::id, Function.identity()));

        // 캐시 미스만 조회
        List<Long> missedIds = articleIds.stream()
            .filter(id -> !map.containsKey(id))
            .toList();

        if (!missedIds.isEmpty()) {
            List<Article> uncached = articleRepository.findAllById(missedIds);
            uncached.forEach(article -> {
                map.put(article.id(), article); // 맵에 병합
                cachedArticleRepository.save(article.id(), article, CACHE_TTL);
            });
        }

        return articleIds.stream()
            .map(map::get)
            .toList();
    }

    public Article read(Long articleId, String ip) {
        // 게시글 조회
        Article article = read(articleId);

        // 게시글 조회 이벤트 발생
        eventPublisher.publishEvent(new ArticleReadEvent(articleId, ip));

        return article;
    }

    private Article read(Long articleId) {
        return cachedArticleRepository.findByKey(articleId)
            .orElseGet(() -> {
                Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFound::new);
                cachedArticleRepository.save(articleId, article, CACHE_TTL);
                return article;
            });
    }

}
