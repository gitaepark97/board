package board.backend.articleRead.application.dto;

import board.backend.article.domain.Article;
import board.backend.user.domain.User;

public record ArticleWithWriterAndCounts(
    Article article,
    User writer,
    Long likeCount,
    Long viewCount,
    Long commentCount
) {

    public static ArticleWithWriterAndCounts of(
        Article article,
        User writer,
        Long likeCount,
        Long viewCount,
        Long commentCount
    ) {
        return new ArticleWithWriterAndCounts(article, writer, likeCount, viewCount, commentCount);
    }

}
