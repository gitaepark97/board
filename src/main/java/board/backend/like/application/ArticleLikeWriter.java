package board.backend.like.application;

import board.backend.article.application.ArticleReader;
import board.backend.common.event.ArticleLikeCountIncreasedEvent;
import board.backend.common.support.TimeProvider;
import board.backend.like.application.port.ArticleLikeCountRepository;
import board.backend.like.application.port.ArticleLikeRepository;
import board.backend.like.domain.ArticleLike;
import board.backend.like.domain.ArticleLikeCount;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleLikeWriter {

    private final TimeProvider timeProvider;
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;
    private final ArticleReader articleReader;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    void like(Long articleId, Long userId) {
        if (!articleLikeRepository.existsByArticleIdAndUserId(articleId, userId)) {
            // 게시글 존재 확인
            articleReader.checkArticleExistsOrThrow(articleId);

            // 게시글 좋아요 생성
            ArticleLike articleLike = ArticleLike.create(articleId, userId, timeProvider.now());
            // 게시글 좋아요 저장
            articleLikeRepository.save(articleLike);

            // 게시글 좋아요 수 증가
            ArticleLikeCount articleLikeCount = ArticleLikeCount.init(articleId);
            articleLikeCountRepository.increaseOrSave(articleLikeCount.articleId(), articleLikeCount.likeCount());

            // 게시글 좋아요 생성 이벤트 발행
            applicationEventPublisher.publishEvent(new ArticleLikeCountIncreasedEvent(articleId, articleLike.createdAt()));
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
            applicationEventPublisher.publishEvent(new ArticleLikeCountIncreasedEvent(articleId, timeProvider.now()));
        });
    }

    @Transactional
    void deleteArticle(Long articleId) {
        // 게시글 좋아요 삭제
        articleLikeRepository.deleteByArticleId(articleId);

        // 게시글 좋아요 수 삭제
        articleLikeCountRepository.deleteById(articleId);
    }

}
