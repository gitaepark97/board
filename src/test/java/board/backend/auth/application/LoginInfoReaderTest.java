package board.backend.auth.application;

import board.backend.auth.domain.LoginInfo;
import board.backend.auth.domain.LoginInfoNotFound;
import board.backend.auth.domain.LoginMethod;
import board.backend.auth.domain.WrongPassword;
import board.backend.auth.infra.LoginInfoRepository;
import board.backend.auth.infra.PasswordEncoderProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginInfoReaderTest {

    private LoginInfoRepository loginInfoRepository;
    private PasswordEncoderProvider passwordEncoderProvider;
    private LoginInfoReader loginInfoReader;

    @BeforeEach
    void setUp() {
        loginInfoRepository = mock(LoginInfoRepository.class);
        passwordEncoderProvider = mock(PasswordEncoderProvider.class);
        loginInfoReader = new LoginInfoReader(loginInfoRepository, passwordEncoderProvider);
    }

    @Test
    @DisplayName("로그인 정보가 존재하고 비밀번호가 일치하면 조회에 성공한다")
    void read_success() {
        // given
        String email = "test@example.com";
        String rawPassword = "password123";
        String encodedPassword = "hashed-password";
        LoginInfo loginInfo = LoginInfo.create(1L, email, encodedPassword, 1L, LocalDateTime.now());

        when(loginInfoRepository.findByLoginMethodAndLoginKey(LoginMethod.EMAIL, email)).thenReturn(Optional.of(loginInfo));
        when(passwordEncoderProvider.matches(rawPassword, encodedPassword)).thenReturn(true);

        // when
        LoginInfo result = loginInfoReader.read(email, rawPassword);

        // then
        assertThat(result).isEqualTo(loginInfo);
    }

    @Test
    @DisplayName("로그인 정보가 존재하지 않으면 예외가 발생한다")
    void read_failWhenLoginInfoNotFound() {
        // given
        String email = "nonexistent@example.com";
        String password = "password123";

        when(loginInfoRepository.findByLoginMethodAndLoginKey(LoginMethod.EMAIL, email)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> loginInfoReader.read(email, password))
            .isInstanceOf(LoginInfoNotFound.class);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 예외가 발생한다")
    void read_failWhenWrongPassword() {
        // given
        String email = "test@example.com";
        String rawPassword = "wrong-password";
        String encodedPassword = "hashed-password";
        LoginInfo loginInfo = LoginInfo.create(1L, email, encodedPassword, 1L, LocalDateTime.now());

        when(loginInfoRepository.findByLoginMethodAndLoginKey(LoginMethod.EMAIL, email)).thenReturn(Optional.of(loginInfo));
        when(passwordEncoderProvider.matches(rawPassword, encodedPassword)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> loginInfoReader.read(email, rawPassword))
            .isInstanceOf(WrongPassword.class);
    }

}