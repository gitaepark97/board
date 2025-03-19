package example.hugo.domain;

public record ArticleLikeCount(
    Long articleId,
    Long likeCount
) {

    public static ArticleLikeCount init(Long articleId) {
        return new ArticleLikeCount(articleId, 1L);
    }

}
