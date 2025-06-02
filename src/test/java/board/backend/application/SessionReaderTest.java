package board.backend.application;

import board.backend.domain.Session;
import board.backend.domain.SessionInvalid;
import board.backend.infra.SessionRepository;
import board.backend.support.TimeProvider;
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

    private TimeProvider timeProvider;
    private SessionRepository sessionRepository;
    private SessionReader sessionReader;

    @BeforeEach
    void setUp() {
        timeProvider = mock(TimeProvider.class);
        sessionRepository = mock(SessionRepository.class);
        sessionReader = new SessionReader(timeProvider, sessionRepository);
    }

    @Test
    @DisplayName("세션이 유효하면 반환한다")
    void read_success() {
        // given
        String sessionId = "abc123";
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 12, 0);
        Session session = Session.create(sessionId, 100L, createdAt);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(timeProvider.now()).thenReturn(createdAt.plusDays(1));

        // when
        Session result = sessionReader.read(sessionId);

        // then
        assertThat(result).isEqualTo(session);
    }

    @Test
    @DisplayName("세션이 존재하지 않으면 예외가 발생한다")
    void read_failWhenSessionNotFound() {
        // given
        String sessionId = "abc123";
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> sessionReader.read(sessionId))
            .isInstanceOf(SessionInvalid.class);
    }

}