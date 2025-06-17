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
            .viewCount(0L)
            .build();
    }

    public static ArticleViewCount create(Long articleId, Long viewCount) {
        return ArticleViewCount.builder()
            .articleId(articleId)
            .viewCount(viewCount)
            .build();
    }

}
