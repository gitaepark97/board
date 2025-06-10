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
            article.getId().toString(),
            article.getBoardId().toString(),
            article.getWriterId().toString(),
            article.getTitle(),
            article.getContent(),
            article.getCreatedAt(),
            article.getUpdatedAt()
        );
    }

}
