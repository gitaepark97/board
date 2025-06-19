package board.backend.auth.application;

import board.backend.auth.application.fake.FakeSessionRepository;
import board.backend.auth.domain.Session;
import board.backend.common.support.fake.FakeTimeProvider;
import board.backend.common.support.fake.FakeUUIDProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SessionCreatorTest {

    private final String uuid = "uuid-1234";
    private final LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

    private FakeSessionRepository sessionRepository;
    private SessionCreator sessionCreator;

    @BeforeEach
    void setUp() {
        sessionRepository = new FakeSessionRepository();
        sessionCreator = new SessionCreator(
            new FakeUUIDProvider(uuid),
            new FakeTimeProvider(now),
            sessionRepository
        );
    }

    @Test
    @DisplayName("세션을 생성하고 저장한다")
    void create_success_whenValidUserId_createsAndSavesSession() {
        // given
        Long userId = 1L;

        // when
        Session session = sessionCreator.create(userId);

        // then
        assertThat(session.id()).isEqualTo(uuid);
        assertThat(session.userId()).isEqualTo(userId);
        assertThat(session.createdAt()).isEqualTo(now);
        assertThat(sessionRepository.findById("uuid-1234")).contains(session);
    }

}