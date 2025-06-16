package board.backend.articleRead.application.dto;

import board.backend.article.domain.Article;
import board.backend.user.domain.User;

public record ArticleDetail(
    Article article,
    User writer,
    Long likeCount,
    Long viewCount,
    Long commentCount
) {

    public static ArticleDetail of(
        Article article,
        User writer,
        Long likeCount,
        Long viewCount,
        Long commentCount
    ) {
        return new ArticleDetail(article, writer, likeCount, viewCount, commentCount);
    }

}
