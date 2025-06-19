package board.backend.article.application;

import board.backend.article.application.port.ArticleRepository;
import board.backend.article.domain.Article;
import board.backend.common.event.EventPublisher;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleDeletedEventPayload;
import board.backend.common.infra.CachedRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
class ArticleDeleter {

    private final CachedRepository<Article, Long> articleCachedRepository;
    private final ArticleRepository articleRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Optional<Long> delete(Long articleId, Long userId) {
        return articleRepository.findById(articleId).map(article -> {
            // 작성자 확인
            article.checkIsWriter(userId);
            // 게시글 삭제
            articleRepository.delete(article);
            // 게시글 캐시 삭제
            articleCachedRepository.delete(articleId);

            // 게시글 삭제 이벤트 발행
            eventPublisher.publishEvent(EventType.ARTICLE_DELETED, new ArticleDeletedEventPayload(articleId));

            return article.boardId();
        });
    }

}
