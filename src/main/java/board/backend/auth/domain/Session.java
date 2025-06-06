package board.backend.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "user_session")
public class Session {

    public static Duration SESSION_DURATION = Duration.ofDays(7);

    @Id
    private String id;
    private Long userId;
    private Boolean isBlock;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;

    public static Session create(String id, Long userId, LocalDateTime now) {
        return Session.builder()
            .id(id)
            .userId(userId)
            .isBlock(false)
            .expiredAt(now.plus(SESSION_DURATION))
            .createdAt(now)
            .build();
    }

    public void checkBlocked() {
        if (isBlock) {
            throw new SessionInvalid();
        }
    }

    public void checkExpired(LocalDateTime now) {
        if (now.isAfter(expiredAt)) {
            throw new SessionInvalid();
        }
    }

}
