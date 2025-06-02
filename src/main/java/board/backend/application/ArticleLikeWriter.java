package board.backend.application;

import board.backend.domain.ArticleLike;
import board.backend.domain.ArticleLikeCount;
import board.backend.infra.ArticleLikeCountRepository;
import board.backend.infra.ArticleLikeRepository;
import board.backend.support.TimeProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleLikeWriter {

    private final TimeProvider timeProvider;
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;

    @Transactional
    void like(Long articleId, Long userId) {
        if (!articleLikeRepository.existsByArticleIdAndUserId(articleId, userId)) {
            // 게시글 좋아요 생성
            ArticleLike articleLike = ArticleLike.create(articleId, userId, timeProvider.now());
            // 게시글 좋아요 저장
            articleLikeRepository.save(articleLike);

            // 게시글 좋아요 수 증가
            long result = articleLikeCountRepository.increase(articleId);
            if (result == 0) {
                articleLikeCountRepository.save(ArticleLikeCount.init(articleId));
            }
        }
    }

    @Transactional
    void unlike(Long articleId, Long userId) {
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId).ifPresent(articleLike -> {
            // 게시글 좋아요 삭제
            articleLikeRepository.delete(articleLike);

            // 게시글 좋아요 수 감소
            articleLikeCountRepository.decrease(articleId);
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
