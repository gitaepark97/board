package board.backend.user.application;

import board.backend.common.infra.fake.FakeCachedRepository;
import board.backend.common.support.fake.FakeTimeProvider;
import board.backend.user.application.fake.FakeUserRepository;
import board.backend.user.domain.User;
import board.backend.user.domain.UserNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserUpdaterTest {

    private final LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

    private FakeCachedRepository<User, Long> cachedUserRepository;
    private FakeUserRepository userRepository;
    private UserUpdater userUpdater;

    @BeforeEach
    void setUp() {
        FakeTimeProvider timeProvider = new FakeTimeProvider(now);
        cachedUserRepository = new FakeCachedRepository<>();
        userRepository = new FakeUserRepository();
        userUpdater = new UserUpdater(timeProvider, cachedUserRepository, userRepository);
    }

    @Test
    @DisplayName("닉네임을 수정할 수 있다")
    void update_success_whenUserExists_updatesNickname() {
        // given
        Long userId = 1L;
        String originalNickname = "original";
        String newNickname = "updated";

        User user = User.create(userId, "test@example.com", originalNickname, LocalDateTime.of(2023, 12, 31, 10, 0));
        userRepository.save(user);
        cachedUserRepository.save(userId, user, Duration.ofMinutes(10));

        // when
        User updated = userUpdater.update(userId, newNickname);

        // then
        assertThat(updated.nickname()).isEqualTo(newNickname);
        assertThat(updated.createdAt()).isEqualTo(user.createdAt());
        assertThat(updated.updatedAt()).isEqualTo(now);

        // and: 저장소에 반영되었는지 확인
        assertThat(userRepository.findById(userId)).contains(updated);

        // and: 캐시가 삭제되었는지 확인
        assertThat(cachedUserRepository.findByKey(userId)).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 사용자일 경우 예외가 발생한다")
    void update_fail_whenUserNotFound_throwsUserNotFound() {
        // given
        Long nonExistentUserId = 999L;

        // when & then
        assertThatThrownBy(() -> userUpdater.update(nonExistentUserId, "nickname"))
            .isInstanceOf(UserNotFound.class);
    }

}