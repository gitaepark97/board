package board.backend.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private final LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

    @Test
    @DisplayName("create 메서드는 주어진 값으로 User를 생성한다")
    void create_success_createsUserWithGivenValues() {
        // given
        Long id = 1L;
        String email = "test@example.com";
        String nickname = "tester";

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
    @DisplayName("update 메서드는 닉네임과 updatedAt을 수정한 새로운 User를 반환한다")
    void update_success_updatesNicknameAndUpdatedAt() {
        // given
        User user = User.create(1L, "test@example.com", "oldNickname", now);

        String newNickname = "newNickname";
        LocalDateTime newTime = now.plusHours(1);

        // when
        User updatedUser = user.update(newNickname, newTime);

        // then
        assertThat(updatedUser.nickname()).isEqualTo(newNickname);
        assertThat(updatedUser.updatedAt()).isEqualTo(newTime);
        assertThat(updatedUser.createdAt()).isEqualTo(now);
    }

}