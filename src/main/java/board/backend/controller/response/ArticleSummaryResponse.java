package board.backend.controller.response;

import board.backend.domain.Article;

import java.time.LocalDateTime;

public record ArticleSummaryResponse(
    Long id,
    Long boardId,
    Long writerId,
    String title,
    LocalDateTime createdAt
) {

    public static ArticleSummaryResponse of(Article article) {
        return new ArticleSummaryResponse(
            article.getId(),
            article.getBoardId(),
            article.getWriterId(),
            article.getTitle(),
            article.getCreatedAt()
        );
    }

}
