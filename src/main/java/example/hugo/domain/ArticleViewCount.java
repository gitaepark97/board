package example.hugo.domain;

public record ArticleViewCount(
    Long articleId,
    Long viewCount
) {

    public static ArticleViewCount init(Long articleId, Long viewCount) {
        return new ArticleViewCount(articleId, viewCount);
    }

}
