package board.backend.domain;

public record ArticleWithLikeCount(
    Article article,
    Long likeCount
) {

    public static ArticleWithLikeCount of(Article article, Long likeCount) {
        return new ArticleWithLikeCount(article, likeCount);
    }

}
