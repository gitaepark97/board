package example.hugo.domain;

public record ArticleViewCount(
    Long articleId,
    Long viewCount
) {

    public static ArticleViewCount init(Long articleId) {
        return new ArticleViewCount(articleId, 1L);
    }

}
