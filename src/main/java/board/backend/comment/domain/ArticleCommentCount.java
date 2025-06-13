package board.backend.comment.domain;

import lombok.Builder;

@Builder
public record ArticleCommentCount(
    Long articleId,
    Long commentCount
) {

    public static ArticleCommentCount init(Long articleId) {
        return ArticleCommentCount.builder()
            .articleId(articleId)
            .commentCount(1L)
            .build();
    }

}
