package board.backend.auth.application;

import board.backend.auth.domain.Session;
import board.backend.auth.infra.SessionRepository;
import board.backend.common.support.TimeProvider;
import board.backend.common.support.UUIDProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SessionWriterTest {

    private UUIDProvider uuidProvider;
    private TimeProvider timeProvider;
    private SessionWriter sessionWriter;

    @BeforeEach
    void setUp() {
        uuidProvider = mock(UUIDProvider.class);
        timeProvider = mock(TimeProvider.class);
        SessionRepository sessionRepository = mock(SessionRepository.class);
        sessionWriter = new SessionWriter(uuidProvider, timeProvider, sessionRepository);
    }

    @Test
    @DisplayName("세션 생성 시 기존 세션을 삭제하고 새 세션을 저장한다")
    void createSession_success() {
        // given
        Long userId = 100L;
        String sessionId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

        when(uuidProvider.random()).thenReturn(sessionId);
        when(timeProvider.now()).thenReturn(now);

        // when
        Session result = sessionWriter.create(userId);

        // then
        assertThat(result.getId()).isEqualTo(sessionId);
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getCreatedAt()).isEqualTo(now);
        assertThat(result.getExpiredAt()).isEqualTo(now.plus(Session.SESSION_DURATION));
    }

    @Test
    @DisplayName("세션 삭제 시 사용자 ID로 삭제를 수행한다")
    void deleteSession_success() {
        // given
        Long userId = 100L;

        // when
        sessionWriter.delete(userId);
    }

}