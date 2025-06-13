package board.backend.like.domain;

import lombok.Builder;

@Builder
public record ArticleLikeCount(
    Long articleId,
    Long likeCount
) {

    public static ArticleLikeCount init(Long articleId) {
        return ArticleLikeCount.builder()
            .articleId(articleId)
            .likeCount(1L)
            .build();
    }

}
