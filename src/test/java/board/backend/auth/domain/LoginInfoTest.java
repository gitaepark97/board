package board.backend.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


class LoginInfoTest {

    private final LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

    @Test
    @DisplayName("이메일 로그인 정보를 생성할 수 있다")
    void create_success_returnsLoginInfo() {
        // given
        Long id = 1L;
        String email = "user@example.com";
        String password = "encrypted_password";
        Long userId = 10L;

        // when
        LoginInfo loginInfo = LoginInfo.create(id, email, password, userId, now);

        // then
        assertThat(loginInfo.id()).isEqualTo(id);
        assertThat(loginInfo.userId()).isEqualTo(userId);
        assertThat(loginInfo.method()).isEqualTo(LoginMethod.EMAIL);
        assertThat(loginInfo.key()).isEqualTo(email);
        assertThat(loginInfo.password()).isEqualTo(password);
        assertThat(loginInfo.createdAt()).isEqualTo(now);
        assertThat(loginInfo.updatedAt()).isEqualTo(now);
    }

}