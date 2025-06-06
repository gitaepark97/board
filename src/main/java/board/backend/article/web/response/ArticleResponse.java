package board.backend.article.web.response;

import board.backend.article.domain.Article;

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

    public static ArticleResponse from(Article article) {
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
