package example.hugo.domain;

public record ArticleCommentCount(
    Long articleId,
    Long commentCount
) {

    public static ArticleCommentCount init(Long articleId) {
        return new ArticleCommentCount(articleId, 1L);
    }

}
