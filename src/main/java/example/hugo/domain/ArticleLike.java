package example.hugo.domain;

import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record ArticleLike(
    Long articleLikeId,
    Long articleId,
    Long userId,
    LocalDateTime createdAt
) {

    public static ArticleLike create(Long articleLikeId, Long articleId, Long userId, LocalDateTime now) {
        return ArticleLike.builder()
            .articleLikeId(articleLikeId)
            .articleId(articleId)
            .userId(userId)
            .createdAt(now)
            .build();
    }

}
