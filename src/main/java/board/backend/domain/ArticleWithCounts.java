package board.backend.domain;

public record ArticleWithCounts(
    Article article,
    Long likeCount,
    Long commentCount
) {

    public static ArticleWithCounts of(Article article, Long likeCount, Long commentCount) {
        return new ArticleWithCounts(article, likeCount, commentCount);
    }

}
