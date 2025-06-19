package board.backend.auth.application;

import board.backend.auth.application.fake.FakeSessionRepository;
import board.backend.auth.domain.Session;
import board.backend.auth.domain.SessionInvalid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SessionReaderTest {

    private FakeSessionRepository sessionRepository;
    private SessionReader sessionReader;

    @BeforeEach
    void setUp() {
        sessionRepository = new FakeSessionRepository();
        sessionReader = new SessionReader(sessionRepository);
    }

    @Test
    @DisplayName("세션 ID로 세션을 조회한다")
    void read_success_whenValidSessionId_returnsSession() {
        // given
        Session session = Session.create("session-123", 1L, LocalDateTime.now());
        sessionRepository.save(session);

        // when
        Session result = sessionReader.read("session-123");

        // then
        assertThat(result).isEqualTo(session);
    }

    @Test
    @DisplayName("세션 ID가 존재하지 않으면 예외가 발생한다")
    void read_fail_whenSessionNotFound_throwsSessionInvalid() {
        // given
        String invalidSessionId = "invalid-session";

        // when & then
        assertThatThrownBy(() -> sessionReader.read(invalidSessionId))
            .isInstanceOf(SessionInvalid.class);
    }

}