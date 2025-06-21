package board.backend.comment.domain;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ArticleCommentCountSnapshot(
    LocalDate date,
    Long articleId,
    Long commentCount
) {

    public static ArticleCommentCountSnapshot create(LocalDate date, Long articleId, Long commentCount) {
        return ArticleCommentCountSnapshot.builder()
            .date(date)
            .articleId(articleId)
            .commentCount(commentCount)
            .build();
    }

}
