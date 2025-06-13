package board.backend.article.web.response;

import board.backend.article.domain.Article;

import java.time.LocalDateTime;

public record ArticleResponse(
    String id,
    String boardId,
    String writerId,
    String title,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ArticleResponse from(Article article) {
        return new ArticleResponse(
            article.id().toString(),
            article.boardId().toString(),
            article.writerId().toString(),
            article.title(),
            article.content(),
            article.createdAt(),
            article.updatedAt()
        );
    }

}
