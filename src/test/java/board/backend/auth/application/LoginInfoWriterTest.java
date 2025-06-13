package board.backend.auth.application;

import board.backend.auth.application.port.LoginInfoRepository;
import board.backend.auth.domain.LoginInfoDuplicated;
import board.backend.auth.domain.LoginMethod;
import board.backend.auth.infra.PasswordEncoderProvider;
import board.backend.common.support.IdProvider;
import board.backend.common.support.TimeProvider;
import board.backend.user.application.UserReader;
import board.backend.user.application.UserWriter;
import board.backend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginInfoWriterTest {

    private IdProvider idProvider;
    private TimeProvider timeProvider;
    private LoginInfoRepository loginInfoRepository;
    private PasswordEncoderProvider passwordEncoderProvider;
    private UserReader userReader;
    private UserWriter userWriter;
    private LoginInfoWriter loginInfoWriter;

    @BeforeEach
    void setUp() {
        idProvider = mock(IdProvider.class);
        timeProvider = mock(TimeProvider.class);
        loginInfoRepository = mock(LoginInfoRepository.class);
        passwordEncoderProvider = mock(PasswordEncoderProvider.class);
        userReader = mock(UserReader.class);
        userWriter = mock(UserWriter.class);
        loginInfoWriter = new LoginInfoWriter(idProvider, timeProvider, loginInfoRepository, passwordEncoderProvider, userReader, userWriter);
    }

    @Test
    @DisplayName("회원이 없고 로그인 정보가 중복되지 않으면 회원 생성 및 로그인 정보 생성에 성공한다")
    void create_successWhenUserNotExists() {
        // given
        String email = "test@example.com";
        String password = "password123";
        String nickname = "닉네임";
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);
        Long userId = 1L;
        Long loginInfoId = 100L;
        User newUser = User.create(userId, email, nickname, now);

        when(userReader.read(email)).thenReturn(Optional.empty());
        when(userWriter.create(email, nickname)).thenReturn(newUser);
        when(loginInfoRepository.existsBy(LoginMethod.EMAIL, email)).thenReturn(false);
        when(idProvider.nextId()).thenReturn(loginInfoId);
        when(passwordEncoderProvider.encode(password)).thenReturn("encoded");
        when(timeProvider.now()).thenReturn(now);

        // when
        loginInfoWriter.create(email, password, nickname);
    }

    @Test
    @DisplayName("이미 로그인 정보가 존재하면 예외가 발생한다")
    void create_failWhenLoginInfoDuplicated() {
        // given
        String email = "test@example.com";
        String password = "password123";
        String nickname = "닉네임";
        User user = mock(User.class);

        when(userReader.read(email)).thenReturn(Optional.of(user));
        when(loginInfoRepository.existsBy(LoginMethod.EMAIL, email)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> loginInfoWriter.create(email, password, nickname))
            .isInstanceOf(LoginInfoDuplicated.class);
    }

}