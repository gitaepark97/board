package example.hugo.web.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import example.hugo.domain.Article;

import java.time.LocalDateTime;

public record ArticleResponse(
    Long articleId,
    String title,
    String content,
    Long boardId,
    Long writerId,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updatedAt
) {

    public static ArticleResponse from(Article article) {
        return new ArticleResponse(
            article.articleId(),
            article.title(),
            article.content(),
            article.boardId(),
            article.writerId(),
            article.createdAt(),
            article.updatedAt()
        );
    }

}
