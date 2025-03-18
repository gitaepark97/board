package example.hugo.domain;

public record ArticleLikeCount(
    Long articleId,
    Long likeCount
) {

    public static ArticleLikeCount init(Long articleId, Long likeCount) {
        return new ArticleLikeCount(articleId, likeCount);
    }

    public ArticleLikeCount increase() {
        return new ArticleLikeCount(articleId, likeCount + 1);
    }

    public ArticleLikeCount decrease() {
        return new ArticleLikeCount(articleId, likeCount - 1);
    }

}
