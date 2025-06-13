package board.backend.view.domain;

import lombok.Builder;

@Builder
public record ArticleViewCount(
    Long articleId,
    Long viewCount
) {

    public static ArticleViewCount init(Long articleId) {
        return ArticleViewCount.builder()
            .articleId(articleId)
            .viewCount(1L)
            .build();
    }

}
