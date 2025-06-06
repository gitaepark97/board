package board.backend.application.dto;

import board.backend.domain.Article;
import board.backend.domain.User;

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
