package board.backend.like.application;

import board.backend.article.application.ArticleValidator;
import board.backend.common.event.EventPublisher;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleLikedEventPaylod;
import board.backend.common.event.payload.ArticleUnlikedEventPayload;
import board.backend.common.support.TimeProvider;
import board.backend.like.application.port.ArticleLikeCountRepository;
import board.backend.like.application.port.ArticleLikeRepository;
import board.backend.like.domain.ArticleLike;
import board.backend.like.domain.ArticleLikeCount;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleLikeManager {

    private final TimeProvider timeProvider;
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;
    private final EventPublisher eventPublisher;
    private final ArticleValidator articleValidator;

    @Transactional
    void like(Long articleId, Long userId) {
        if (!articleLikeRepository.existsByArticleIdAndUserId(articleId, userId)) {
            // 게시글 존재 확인
            articleValidator.checkArticleExistsOrThrow(articleId);

            // 게시글 좋아요 생성
            ArticleLike articleLike = ArticleLike.create(articleId, userId, timeProvider.now());
            // 게시글 좋아요 저장
            articleLikeRepository.save(articleLike);

            // 게시글 좋아요 수 증가
            ArticleLikeCount articleLikeCount = ArticleLikeCount.init(articleId);
            articleLikeCountRepository.increaseOrSave(articleLikeCount);

            // 게시글 좋아요 생성 이벤트 발행
            eventPublisher.publishEvent(EventType.ARTICLE_LIKED, new ArticleLikedEventPaylod(articleId, articleLike.createdAt()));
        }
    }

    @Transactional
    void unlike(Long articleId, Long userId) {
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId).ifPresent(articleLike -> {
            // 게시글 좋아요 삭제
            articleLikeRepository.delete(articleLike);

            // 게시글 좋아요 수 감소
            articleLikeCountRepository.decrease(articleId);

            // 게시글 좋아요 삭제 이벤트 발행
            eventPublisher.publishEvent(EventType.ARTICLE_UNLIKED, new ArticleUnlikedEventPayload(articleId, timeProvider.now()));
        });
    }

}
