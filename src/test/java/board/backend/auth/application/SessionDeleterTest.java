package board.backend.auth.application;

import board.backend.auth.application.fake.FakeSessionRepository;
import board.backend.auth.domain.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SessionDeleterTest {

    private FakeSessionRepository sessionRepository;
    private SessionDeleter sessionDeleter;

    @BeforeEach
    void setUp() {
        sessionRepository = new FakeSessionRepository();
        sessionDeleter = new SessionDeleter(sessionRepository);
    }

    @Test
    @DisplayName("사용자 ID로 세션을 삭제한다")
    void delete_success_whenUserIdExists_deletesSessionByUserId() {
        // given
        Session session = Session.create("uuid-1", 10L, LocalDateTime.now());
        sessionRepository.save(session);

        // when
        sessionDeleter.delete(session.userId());

        // then
        assertThat(sessionRepository.findById("uuid-1")).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID를 삭제해도 예외는 발생하지 않는다")
    void delete_success_whenUserIdNotExists_doesNothing() {
        // given
        Session session = Session.create("uuid-2", 20L, LocalDateTime.now());
        sessionRepository.save(session);

        // when
        sessionDeleter.delete(999L);

        // then
        assertThat(sessionRepository.findById("uuid-2")).contains(session);
    }

}