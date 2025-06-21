package board.backend.like.domain;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ArticleLikeCountSnapshot(
    LocalDate date,
    Long articleId,
    Long likeCount
) {

    public static ArticleLikeCountSnapshot create(LocalDate date, Long articleId, Long likeCount) {
        return ArticleLikeCountSnapshot.builder()
            .date(date)
            .articleId(articleId)
            .likeCount(likeCount)
            .build();
    }

}
