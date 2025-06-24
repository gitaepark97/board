package board.backend.auth.application;

import board.backend.auth.application.fake.FakeLoginInfoRepository;
import board.backend.auth.application.fake.FakePasswordEncoderProvider;
import board.backend.auth.domain.LoginInfo;
import board.backend.auth.domain.LoginInfoDuplicated;
import board.backend.auth.domain.LoginMethod;
import board.backend.common.cache.fake.FakeCachedRepository;
import board.backend.common.support.fake.FakeIdProvider;
import board.backend.common.support.fake.FakeTimeProvider;
import board.backend.user.application.UserCreator;
import board.backend.user.application.UserReader;
import board.backend.user.application.fake.FakeUserRepository;
import board.backend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoginInfoCreatorTest {

    private final Long id = 1L;
    private final LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

    private FakeLoginInfoRepository loginInfoRepository;
    private FakeUserRepository userRepository;
    private LoginInfoCreator loginInfoCreator;

    @BeforeEach
    void setUp() {
        FakeIdProvider idProvider = new FakeIdProvider(id);
        FakeTimeProvider timeProvider = new FakeTimeProvider(now);
        loginInfoRepository = new FakeLoginInfoRepository();
        userRepository = new FakeUserRepository();
        loginInfoCreator = new LoginInfoCreator(
            idProvider,
            timeProvider,
            loginInfoRepository,
            new FakePasswordEncoderProvider(),
            new UserReader(new FakeCachedRepository<>(), userRepository),
            new UserCreator(idProvider, timeProvider, userRepository)
        );
    }

    @Test
    @DisplayName("사용자이 존재하지 않으면 새로 생성하고 로그인 정보를 저장한다")
    void create_success_whenUserNotExists_createsUserAndLoginInfo() {
        // given
        String email = "new@example.com";
        String password = "secret";
        String nickname = "newbie";

        // when
        loginInfoCreator.create(email, password, nickname);

        // then
        User user = userRepository.findByEmail(email).orElseThrow();
        LoginInfo loginInfo = loginInfoRepository.findByLoginMethodAndLoginKey(LoginMethod.EMAIL, email).orElseThrow();

        assertThat(user.nickname()).isEqualTo(nickname);
        assertThat(loginInfo.id()).isEqualTo(id);
        assertThat(loginInfo.userId()).isEqualTo(user.id());
        assertThat(loginInfo.password()).isEqualTo("encoded:" + password);
        assertThat(loginInfo.createdAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("사용자이 이미 존재하면 재사용하고 로그인 정보를 저장한다")
    void create_success_whenUserExists_usesExistingUser() {
        // given
        String email = "exist@example.com";
        String password = "pw";
        String nickname = "irrelevant";
        User existing = User.create(42L, email, "origin", LocalDateTime.of(2023, 12, 1, 10, 0));
        userRepository.save(existing);

        // when
        loginInfoCreator.create(email, password, nickname);

        // then
        LoginInfo loginInfo = loginInfoRepository.findByLoginMethodAndLoginKey(LoginMethod.EMAIL, email).orElseThrow();
        assertThat(loginInfo.userId()).isEqualTo(existing.id());
        assertThat(userRepository.findAllById(List.of(42L))).hasSize(1); // 새로 생성되지 않음
    }

    @Test
    @DisplayName("이미 로그인 정보가 존재하면 예외가 발생한다")
    void create_fail_whenLoginInfoAlreadyExists_throwsLoginInfoDuplicated() {
        // given
        String email = "dup@example.com";
        String password = "pw";
        String nickname = "dup";
        User user = User.create(99L, email, nickname, LocalDateTime.now());
        LoginInfo existing = LoginInfo.create(100L, email, "encoded:" + password, user.id(), LocalDateTime.now());
        userRepository.save(user);
        loginInfoRepository.save(existing);

        // when & then
        assertThatThrownBy(() -> loginInfoCreator.create(email, password, nickname))
            .isInstanceOf(LoginInfoDuplicated.class);
    }

}