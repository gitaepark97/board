package board.backend.controller.response;

import board.backend.domain.Article;

import java.time.LocalDateTime;

public record ArticleResponse(
    Long id,
    Long boardId,
    Long writerId,
    String title,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ArticleResponse of(Article article) {
        return new ArticleResponse(
            article.getId(),
            article.getBoardId(),
            article.getWriterId(),
            article.getTitle(),
            article.getContent(),
            article.getCreatedAt(),
            article.getUpdatedAt()
        );
    }

}
