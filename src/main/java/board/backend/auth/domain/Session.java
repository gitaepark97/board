package board.backend.auth.domain;

import lombok.Builder;
import org.springframework.data.redis.core.RedisHash;

import java.time.Duration;
import java.time.LocalDateTime;

@Builder
@RedisHash(value = "session", timeToLive = 60 * 60 * 24 * 7L)
public record Session(
    String id,
    Long userId,
    Boolean isBlock,
    LocalDateTime createdAt
) {

    public static Duration SESSION_DURATION = Duration.ofDays(7);

    public static Session create(String id, Long userId, LocalDateTime now) {
        return Session.builder()
            .id(id)
            .userId(userId)
            .isBlock(false)
            .createdAt(now)
            .build();
    }

    public void checkBlocked() {
        if (isBlock) {
            throw new SessionInvalid();
        }
    }

}
