package example.hugo.application;

import example.hugo.domain.ArticleLike;
import example.hugo.domain.ArticleLikeCount;
import example.hugo.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleLikeReader {

    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;

    ArticleLike readArticleLike(Long articleId, Long userId) {
        return articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
            .orElseThrow(ErrorCode.NOT_FOUND_ARTICLE::toException);
    }

    Long countArticleLikes(Long articleId) {
        return articleLikeCountRepository.findByArticleId(articleId)
            .map(ArticleLikeCount::likeCount)
            .orElse(0L);
    }

}
