package board.backend.auth.infra.redis;

import board.backend.auth.domain.Session;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash(value = "session", timeToLive = 60 * 60 * 24 * 7L)
class SessionEntity {

    @Id
    private String id;

    private Long userId;

    private Boolean isBlock;

    private LocalDateTime createdAt;

    static SessionEntity from(Session session) {
        return new SessionEntity(
            session.id(),
            session.userId(),
            session.isBlock(),
            session.createdAt()
        );
    }

    Session toSession() {
        return Session.builder()
            .id(id)
            .userId(userId)
            .isBlock(isBlock)
            .createdAt(createdAt)
            .build();
    }

}
