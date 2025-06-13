package board.backend.like.domain;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ArticleLike(
    Long articleId,
    Long userId,
    LocalDateTime createdAt
) {

    public static ArticleLike create(Long articleId, Long userId, LocalDateTime now) {
        return ArticleLike.builder()
            .articleId(articleId)
            .userId(userId)
            .createdAt(now)
            .build();
    }

}
