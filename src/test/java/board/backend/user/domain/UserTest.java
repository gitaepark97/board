package board.backend.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserTest {

    @Test
    @DisplayName("회원 생성에 성공한다")
    void create_success() {
        // given
        Long id = 1L;
        String email = "user@example.com";
        String nickname = "사용자";
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);

        // when
        User user = User.create(id, email, nickname, now);

        // then
        assertThat(user.id()).isEqualTo(id);
        assertThat(user.email()).isEqualTo(email);
        assertThat(user.nickname()).isEqualTo(nickname);
        assertThat(user.createdAt()).isEqualTo(now);
        assertThat(user.updatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("회원 닉네임 수정에 성공한다")
    void update_success() {
        // given
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        User user = User.create(1L, "user@example.com", "기존닉네임", createdAt);

        String newNickname = "새닉네임";
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 2, 15, 0);

        // when
        User result = user.update(newNickname, updatedAt);

        // then
        assertThat(result.nickname()).isEqualTo(newNickname);
        assertThat(result.updatedAt()).isEqualTo(updatedAt);
        assertThat(result.createdAt()).isEqualTo(createdAt); // 생성 시각은 그대로 유지
    }

}