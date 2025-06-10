package board.backend.auth.domain;

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
        assertThat(session.id()).isEqualTo(id);
        assertThat(session.userId()).isEqualTo(userId);
        assertThat(session.isBlock()).isFalse();
        assertThat(session.createdAt()).isEqualTo(now);
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
            .build();

        // when & then
        assertThatThrownBy(session::checkBlocked)
            .isInstanceOf(SessionInvalid.class);
    }

}