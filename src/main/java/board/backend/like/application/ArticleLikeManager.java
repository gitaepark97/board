package board.backend.like.application;

import board.backend.article.application.ArticleValidator;
import board.backend.common.event.EventPublisher;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleLikeCountChangedEventPayload;
import board.backend.common.exception.InternalServerError;
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
    private final TodayLikeCountCalculatorImpl todayLikeCountCalculator;

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
            articleLikeCountRepository.increase(articleId);

            // 게시글 좋아요 생성 이벤트 발행
            publishEvent(articleLike);
        }
    }

    @Transactional
    void unlike(Long articleId, Long userId) {
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId).ifPresent(articleLike -> {
            // 게시글 좋아요 삭제
            articleLikeRepository.delete(articleLike);

            // 게시글 좋아요 수 감소
            articleLikeCountRepository.decrease(articleId);

            if (timeProvider.isToday(articleLike.createdAt())) {
                publishEvent(articleLike);
            }
        });
    }

    private void publishEvent(ArticleLike articleLike) {
        ArticleLikeCount articleLikeCount = articleLikeCountRepository.findById(articleLike.articleId())
            .orElseThrow(InternalServerError::new);
        long todayCount = todayLikeCountCalculator.calculate(articleLikeCount);
        eventPublisher.publishEvent(EventType.ARTICLE_LIKE_COUNT_CHANGED, new ArticleLikeCountChangedEventPayload(articleLike.articleId(), todayCount, articleLike.createdAt()));
    }

}
