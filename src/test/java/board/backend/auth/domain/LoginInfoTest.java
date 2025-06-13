package board.backend.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LoginInfoTest {

    @Test
    @DisplayName("이메일 로그인 방식의 LoginInfo 생성에 성공한다")
    void create_emailLoginSuccess() {
        // given
        Long id = 1L;
        Long userId = 100L;
        String email = "user@example.com";
        String password = "securePass123";
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);

        // when
        LoginInfo loginInfo = LoginInfo.create(id, email, password, userId, now);

        // then
        assertThat(loginInfo.id()).isEqualTo(id);
        assertThat(loginInfo.userId()).isEqualTo(userId);
        assertThat(loginInfo.loginMethod()).isEqualTo(LoginMethod.EMAIL);
        assertThat(loginInfo.loginKey()).isEqualTo(email);
        assertThat(loginInfo.password()).isEqualTo(password);
        assertThat(loginInfo.createdAt()).isEqualTo(now);
        assertThat(loginInfo.updatedAt()).isEqualTo(now);
    }

}