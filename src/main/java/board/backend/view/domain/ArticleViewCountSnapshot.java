package board.backend.view.domain;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ArticleViewCountSnapshot(
    LocalDate date,
    Long articleId,
    Long viewCount
) {

    public static ArticleViewCountSnapshot create(LocalDate date, Long articleId, Long viewCount) {
        return ArticleViewCountSnapshot.builder()
            .date(date)
            .articleId(articleId)
            .viewCount(viewCount)
            .build();
    }

}
