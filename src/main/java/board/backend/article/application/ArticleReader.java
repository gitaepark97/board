package board.backend.article.application;

import board.backend.article.application.port.ArticleRepository;
import board.backend.article.domain.Article;
import board.backend.article.domain.ArticleNotFound;
import board.backend.common.event.EventPublisher;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleReadEventPayload;
import board.backend.common.infra.CachedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static board.backend.article.application.ArticleConstants.ARTICLE_CACHE_TTL;

@NamedInterface
@RequiredArgsConstructor
@Component
public class ArticleReader {

    private final CachedRepository<Article, Long> cachedArticleRepository;
    private final ArticleRepository articleRepository;
    private final EventPublisher eventPublisher;

    public List<Article> readAll(Long boardId, Long pageSize, Long lastArticleId) {
        return lastArticleId == null ?
            articleRepository.findAllByBoardId(boardId, pageSize) :
            articleRepository.findAllByBoardId(boardId, pageSize, lastArticleId);
    }

    public List<Article> readAll(List<Long> articleIds) {
        // 캐시 조회
        List<Article> articles = cachedArticleRepository.findAllByKey(articleIds);

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
                cachedArticleRepository.save(article.id(), article, ARTICLE_CACHE_TTL);
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
        eventPublisher.publishEvent(EventType.ARTICLE_READ, new ArticleReadEventPayload(articleId, ip));

        return article;
    }

    private Article read(Long articleId) {
        return cachedArticleRepository.findByKey(articleId)
            .orElseGet(() -> {
                Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFound::new);
                cachedArticleRepository.save(articleId, article, ARTICLE_CACHE_TTL);
                return article;
            });
    }

}
