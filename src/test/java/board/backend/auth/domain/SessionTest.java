package board.backend.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SessionTest {

    private final LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

    @Test
    @DisplayName("세션을 생성할 수 있다")
    void create_success_returnsSession() {
        // given
        String sessionId = "session-uuid";
        Long userId = 1L;

        // when
        Session session = Session.create(sessionId, userId, now);

        // then
        assertThat(session.id()).isEqualTo(sessionId);
        assertThat(session.userId()).isEqualTo(userId);
        assertThat(session.isBlock()).isFalse();
        assertThat(session.createdAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("차단된 세션이면 예외가 발생한다")
    void checkBlocked_fail_whenBlocked_throwsException() {
        // given
        Session session = Session.builder()
            .id("session-uuid")
            .userId(1L)
            .isBlock(true)
            .createdAt(now)
            .build();

        // when & then
        assertThatThrownBy(session::checkBlocked)
            .isInstanceOf(SessionInvalid.class);
    }

    @Test
    @DisplayName("차단되지 않은 세션은 예외 없이 통과된다")
    void checkBlocked_success_whenNotBlocked_doesNothing() {
        // given
        Session session = Session.builder()
            .id("session-uuid")
            .userId(1L)
            .isBlock(false)
            .createdAt(now)
            .build();

        // when & then
        session.checkBlocked();
    }

    @Test
    @DisplayName("세션 유지 기간은 7일이다")
    void sessionDuration_is7Days() {
        // then
        assertThat(Session.SESSION_DURATION).isEqualTo(Duration.ofDays(7));
    }

}