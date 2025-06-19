package board.backend.auth.application;

import board.backend.auth.application.fake.FakeLoginInfoRepository;
import board.backend.auth.application.fake.FakePasswordEncoderProvider;
import board.backend.auth.domain.LoginInfo;
import board.backend.auth.domain.LoginInfoNotFound;
import board.backend.auth.domain.WrongPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoginInfoReaderTest {

    private FakeLoginInfoRepository loginInfoRepository;
    private FakePasswordEncoderProvider passwordEncoderProvider;
    private LoginInfoReader loginInfoReader;

    @BeforeEach
    void setUp() {
        loginInfoRepository = new FakeLoginInfoRepository();
        passwordEncoderProvider = new FakePasswordEncoderProvider();
        loginInfoReader = new LoginInfoReader(loginInfoRepository, passwordEncoderProvider);
    }

    @Test
    @DisplayName("이메일과 비밀번호가 일치하면 로그인 정보를 반환한다")
    void read_success_whenEmailAndPasswordMatch_returnsLoginInfo() {
        // given
        String email = "user@example.com";
        String password = "1234";
        String encodedPassword = passwordEncoderProvider.encode(password);
        LoginInfo loginInfo = LoginInfo.create(1L, email, encodedPassword, 10L, LocalDateTime.now());
        loginInfoRepository.save(loginInfo);

        // when
        LoginInfo result = loginInfoReader.read(email, password);

        // then
        assertThat(result).isEqualTo(loginInfo);
    }

    @Test
    @DisplayName("로그인 정보가 존재하지 않으면 예외가 발생한다")
    void read_fail_whenLoginInfoNotFound_throwsLoginInfoNotFound() {
        // given
        String email = "notfound@example.com";
        String password = "pw";

        // when & then
        assertThatThrownBy(() -> loginInfoReader.read(email, password))
            .isInstanceOf(LoginInfoNotFound.class);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 예외가 발생한다")
    void read_fail_whenPasswordDoesNotMatch_throwsWrongPassword() {
        // given
        String email = "user@example.com";
        String correctPassword = "1234";
        String wrongPassword = "wrong";
        LoginInfo loginInfo = LoginInfo.create(1L, email, passwordEncoderProvider.encode(correctPassword), 10L, LocalDateTime.now());
        loginInfoRepository.save(loginInfo);

        // when & then
        assertThatThrownBy(() -> loginInfoReader.read(email, wrongPassword))
            .isInstanceOf(WrongPassword.class);
    }

}