package board.backend.application;

import board.backend.domain.LoginInfo;
import board.backend.domain.LoginInfoNotFound;
import board.backend.domain.LoginMethod;
import board.backend.domain.WrongPassword;
import board.backend.infra.LoginInfoRepository;
import board.backend.infra.PasswordEncoderProvider;
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

    private PasswordEncoderProvider passwordEncoderProvider;
    private LoginInfoRepository loginInfoRepository;
    private LoginInfoReader loginInfoReader;

    @BeforeEach
    void setUp() {
        passwordEncoderProvider = mock(PasswordEncoderProvider.class);
        loginInfoRepository = mock(LoginInfoRepository.class);
        loginInfoReader = new LoginInfoReader(passwordEncoderProvider, loginInfoRepository);
    }

    @Test
    @DisplayName("로그인 정보가 존재하지 않으면 예외가 발생한다")
    void read_failWhenLoginInfoNotFound() {
        // given
        String email = "user@example.com";
        String password = "pw1234";

        when(loginInfoRepository.findByLoginMethodAndLoginKey(LoginMethod.EMAIL, email))
            .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> loginInfoReader.read(email, password))
            .isInstanceOf(LoginInfoNotFound.class);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 예외가 발생한다")
    void read_failWhenWrongPassword() {
        // given
        String email = "user@example.com";
        String password = "wrong_pw";
        LoginInfo loginInfo = LoginInfo.create(1L, email, "encoded_pw", 100L, LocalDateTime.now());

        when(loginInfoRepository.findByLoginMethodAndLoginKey(LoginMethod.EMAIL, email))
            .thenReturn(Optional.of(loginInfo));
        when(passwordEncoderProvider.matches(password, loginInfo.getPassword()))
            .thenReturn(false);

        // when & then
        assertThatThrownBy(() -> loginInfoReader.read(email, password))
            .isInstanceOf(WrongPassword.class);
    }

    @Test
    @DisplayName("이메일과 비밀번호가 일치하면 로그인 정보 조회에 성공한다")
    void read_success() {
        // given
        String email = "user@example.com";
        String password = "correct_pw";
        LoginInfo loginInfo = LoginInfo.create(1L, email, "encoded_pw", 100L, LocalDateTime.now());

        when(loginInfoRepository.findByLoginMethodAndLoginKey(LoginMethod.EMAIL, email))
            .thenReturn(Optional.of(loginInfo));
        when(passwordEncoderProvider.matches(password, loginInfo.getPassword()))
            .thenReturn(true);

        // when
        LoginInfo result = loginInfoReader.read(email, password);

        // then
        assertThat(result).isEqualTo(loginInfo);
    }

}