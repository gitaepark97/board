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
        assertThat(loginInfo.getId()).isEqualTo(id);
        assertThat(loginInfo.getUserId()).isEqualTo(userId);
        assertThat(loginInfo.getLoginMethod()).isEqualTo(LoginMethod.EMAIL);
        assertThat(loginInfo.getLoginKey()).isEqualTo(email);
        assertThat(loginInfo.getPassword()).isEqualTo(password);
        assertThat(loginInfo.getCreatedAt()).isEqualTo(now);
        assertThat(loginInfo.getUpdatedAt()).isEqualTo(now);
    }

}