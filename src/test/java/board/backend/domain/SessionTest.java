package board.backend.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class SessionTest {

    @Test
    @DisplayName("세션 생성에 성공한다")
    void create_success() {
        // given
        String id = "session-uuid";
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);

        // when
        Session session = Session.create(id, userId, now);

        // then
        assertThat(session.getId()).isEqualTo(id);
        assertThat(session.getUserId()).isEqualTo(userId);
        assertThat(session.getIsBlock()).isFalse();
        assertThat(session.getCreatedAt()).isEqualTo(now);
        assertThat(session.getExpiredAt()).isEqualTo(now.plusDays(7));
    }

    @Test
    @DisplayName("차단되지 않은 세션은 checkBlocked() 시 예외가 발생하지 않는다")
    void checkBlocked_notBlocked() {
        // given
        Session session = Session.create("id", 1L, LocalDateTime.now());

        // when
        session.checkBlocked();
    }

    @Test
    @DisplayName("차단된 세션은 checkBlocked() 시 SessionInvalid 예외가 발생한다")
    void checkBlocked_blocked() {
        // given
        Session session = Session.builder()
            .id("id")
            .userId(1L)
            .isBlock(true)
            .createdAt(LocalDateTime.now())
            .expiredAt(LocalDateTime.now().plusDays(7))
            .build();

        // when & then
        assertThatThrownBy(session::checkBlocked)
            .isInstanceOf(SessionInvalid.class);
    }

    @Test
    @DisplayName("만료되지 않은 세션은 checkExpired() 시 예외가 발생하지 않는다")
    void checkExpired_notExpired() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Session session = Session.create("id", 1L, now);

        // when
        session.checkExpired(now);
    }

    @Test
    @DisplayName("만료된 세션은 checkExpired() 시 SessionInvalid 예외가 발생한다")
    void checkExpired_expired() {
        // given
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime expiredAt = createdAt.plusDays(7);
        Session session = Session.builder()
            .id("id")
            .userId(1L)
            .isBlock(false)
            .createdAt(createdAt)
            .expiredAt(expiredAt)
            .build();

        LocalDateTime now = expiredAt.plusSeconds(1);

        // when & then
        assertThatThrownBy(() -> session.checkExpired(now))
            .isInstanceOf(SessionInvalid.class);
    }

}