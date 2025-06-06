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
        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getNickname()).isEqualTo(nickname);
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getUpdatedAt()).isEqualTo(now);
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
        user.update(newNickname, updatedAt);

        // then
        assertThat(user.getNickname()).isEqualTo(newNickname);
        assertThat(user.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(user.getCreatedAt()).isEqualTo(createdAt); // 생성 시각은 그대로 유지
    }

}