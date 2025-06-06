package board.backend.auth.application;

import board.backend.auth.domain.Session;
import board.backend.auth.domain.SessionInvalid;
import board.backend.auth.infra.SessionRepository;
import board.backend.common.support.TimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SessionReaderTest {

    private SessionRepository sessionRepository;
    private TimeProvider timeProvider;
    private SessionReader sessionReader;

    @BeforeEach
    void setUp() {
        sessionRepository = mock(SessionRepository.class);
        timeProvider = mock(TimeProvider.class);
        sessionReader = new SessionReader(timeProvider, sessionRepository);
    }

    @Test
    @DisplayName("세션 조회에 성공한다")
    void read_success() {
        // given
        String sessionId = "abc-123";
        Session session = mock(Session.class);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(timeProvider.now()).thenReturn(LocalDateTime.of(2024, 1, 1, 10, 0));

        // when
        Session result = sessionReader.read(sessionId);

        // then
        assertThat(result).isEqualTo(session);
    }

    @Test
    @DisplayName("세션이 존재하지 않으면 예외가 발생한다")
    void read_fail_notFound() {
        // given
        String invalidId = "invalid";
        when(sessionRepository.findById(invalidId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> sessionReader.read(invalidId))
            .isInstanceOf(SessionInvalid.class);
    }

}