package example.hugo.domain;

public record ArticleCommentCount(
    Long articleId,
    Long commentCount
) {

    public static ArticleCommentCount init(Long articleId, Long commentCount) {
        return new ArticleCommentCount(articleId, commentCount);
    }

}
