package board.backend.article.application;

import board.backend.article.domain.Article;
import board.backend.article.domain.ArticleNotFound;
import board.backend.article.infra.jpa.ArticleRepository;
import board.backend.common.event.ArticleReaderEvent;
import board.backend.common.infra.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@NamedInterface
@RequiredArgsConstructor
@Component
public class ArticleReader {

    private static final Duration CACHE_TTL = Duration.ofMinutes(5);

    private final CacheRepository<Article, Long> articleCacheRepository;
    private final ArticleRepository articleRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void checkArticleExistsOrThrow(Long articleId) {
        if (articleCacheRepository.get(articleId).isEmpty() && !articleRepository.customExistsById(articleId)) {
            throw new ArticleNotFound();
        }
    }

    public List<Article> readAll(Long boardId, Long pageSize, Long lastArticleId) {
        return lastArticleId == null ?
            articleRepository.findAllByBoardId(boardId, pageSize) :
            articleRepository.findAllByBoardId(boardId, pageSize, lastArticleId);
    }

    Article read(Long articleId, String ip) {
        // 게시글 조회
        Article article = read(articleId);

        // 게시글 조회 이벤트 발생
        eventPublisher.publishEvent(new ArticleReaderEvent(articleId, ip));

        return article;
    }

    private Article read(Long articleId) {
        return articleCacheRepository.get(articleId)
            .orElseGet(() -> {
                Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFound::new);
                articleCacheRepository.set(articleId, article, CACHE_TTL);
                return article;
            });
    }

}
