package board.backend.auth.application;

import board.backend.auth.application.dto.Token;
import board.backend.auth.application.fake.FakeTokenProvider;
import board.backend.auth.domain.Session;
import board.backend.common.cache.fake.FakeCachedRepository;
import board.backend.user.application.UserValidator;
import board.backend.user.application.fake.FakeUserRepository;
import board.backend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TokenManagerTest {

    private FakeTokenProvider tokenProvider;
    private FakeUserRepository userRepository;
    private TokenManager tokenManager;

    @BeforeEach
    void setUp() {
        tokenProvider = new FakeTokenProvider();
        userRepository = new FakeUserRepository();
        tokenManager = new TokenManager(
            tokenProvider,
            new UserValidator(new FakeCachedRepository<>(), userRepository)
        );
    }

    @Test
    @DisplayName("세션으로 액세스 토큰을 발급한다")
    void issueToken_success_whenValidSession_returnsToken() {
        // given
        Session session = Session.create("session-1", 100L, LocalDateTime.now());

        // when
        Token token = tokenManager.issueToken(session);

        // then
        assertThat(token.access()).isEqualTo("token-1");
        assertThat(token.refresh()).isEqualTo("session-1");
    }

    @Test
    @DisplayName("유효한 토큰에서 사용자 ID를 추출할 수 있다")
    void getUserId_success_whenUserExists_returnsUserId() {
        // given
        String token = "token-42";
        tokenProvider.addPayload(token, Map.of("userId", "123"));
        userRepository.save(User.create(123L, "email", "nick", LocalDateTime.now()));

        // when
        Optional<Long> result = tokenManager.getUserId(token);

        // then
        assertThat(result).contains(123L);
    }

    @Test
    @DisplayName("사용자가 존재하지 않으면 빈 Optional을 반환한다")
    void getUserId_fail_whenUserNotExists_returnsEmpty() {
        // given
        String token = "token-for-user-999";
        tokenProvider.addPayload(token, Map.of("userId", "999"));

        // when
        Optional<Long> result = tokenManager.getUserId(token);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("잘못된 토큰이면 빈 Optional을 반환한다")
    void getUserId_fail_whenTokenMalformed_returnsEmpty() {
        // given
        String invalidToken = "invalid-token";

        // when
        Optional<Long> result = tokenManager.getUserId(invalidToken);

        // then
        assertThat(result).isEmpty();
    }

}