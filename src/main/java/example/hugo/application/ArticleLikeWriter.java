package example.hugo.application;

import example.hugo.domain.ArticleLike;
import example.hugo.domain.ArticleLikeCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
class ArticleLikeWriter {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;

    @Transactional
    void createArticleLike(Long articleId, Long userId) {
        // 좋아요 생성
        ArticleLike newArticleLike = ArticleLike.create(idProvider.nextId(), articleId, userId, timeProvider.now());
        articleLikeRepository.save(newArticleLike);

        // 게시글 좋아요 수 변경
        long result = articleLikeCountRepository.increase(articleId);
        if (result == 0L) {
            articleLikeCountRepository.save(ArticleLikeCount.init(newArticleLike.articleId()));
        }
    }

    @Transactional
    void deleteArticleLike(Long articleId, Long userId) {
        // 좋아요 조회
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId).ifPresent(existingArticleLike -> {
                // 좋아요 삭제
                articleLikeRepository.delete(existingArticleLike);

                // 게시글 좋아요 수 변경
                articleLikeCountRepository.decrease(existingArticleLike.articleId());
            }
        );
    }

}
