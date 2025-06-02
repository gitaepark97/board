package board.backend.application;

import board.backend.domain.LoginInfoDuplicated;
import board.backend.domain.LoginMethod;
import board.backend.infra.LoginInfoRepository;
import board.backend.infra.PasswordEncoderProvider;
import board.backend.support.IdProvider;
import board.backend.support.TimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginInfoWriterTest {

    private IdProvider idProvider;
    private TimeProvider timeProvider;
    private PasswordEncoderProvider passwordEncoderProvider;
    private LoginInfoRepository loginInfoRepository;
    private LoginInfoWriter loginInfoWriter;

    @BeforeEach
    void setUp() {
        idProvider = mock(IdProvider.class);
        timeProvider = mock(TimeProvider.class);
        passwordEncoderProvider = mock(PasswordEncoderProvider.class);
        loginInfoRepository = mock(LoginInfoRepository.class);
        loginInfoWriter = new LoginInfoWriter(idProvider, timeProvider, passwordEncoderProvider, loginInfoRepository);
    }

    @Test
    @DisplayName("이메일 로그인 정보가 중복되면 예외가 발생한다")
    void create_fail_whenEmailExists() {
        // given
        String email = "test@example.com";
        when(loginInfoRepository.existsByLoginMethodAndLoginKey(LoginMethod.EMAIL, email)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> loginInfoWriter.create(email, "password", 1L))
            .isInstanceOf(LoginInfoDuplicated.class);
    }

    @Test
    @DisplayName("로그인 정보를 저장한다")
    void create_success() {
        // given
        String email = "test@example.com";
        String rawPassword = "password123";
        String encodedPassword = "encoded_pw";
        Long loginInfoId = 1L;
        Long userId = 100L;
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

        when(loginInfoRepository.existsByLoginMethodAndLoginKey(LoginMethod.EMAIL, email)).thenReturn(false);
        when(idProvider.nextId()).thenReturn(loginInfoId);
        when(timeProvider.now()).thenReturn(now);
        when(passwordEncoderProvider.encode(rawPassword)).thenReturn(encodedPassword);

        // when
        loginInfoWriter.create(email, rawPassword, userId);
    }

}