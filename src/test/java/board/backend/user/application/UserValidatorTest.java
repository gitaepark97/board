package board.backend.user.application;

import board.backend.common.cache.fake.FakeCachedRepository;
import board.backend.user.application.fake.FakeUserRepository;
import board.backend.user.domain.User;
import board.backend.user.domain.UserNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserValidatorTest {

    private FakeCachedRepository<User, Long> cachedUserRepository;
    private FakeUserRepository userRepository;
    private UserValidator userValidator;

    @BeforeEach
    void setUp() {
        cachedUserRepository = new FakeCachedRepository<>();
        userRepository = new FakeUserRepository();
        userValidator = new UserValidator(cachedUserRepository, userRepository);
    }

    @Test
    @DisplayName("캐시 또는 DB에 사용자가 존재하면 예외가 발생하지 않는다")
    void checkUserExistsOrThrow_success_whenUserInCacheOrDB_doesNotThrow() {
        // given
        Long userId = 1L;
        userRepository.save(User.create(userId, "email@example.com", "nick", LocalDateTime.now()));

        // when & then
        userValidator.checkUserExistsOrThrow(userId);

        // and: null 이라도 캐시에 저장되었는지 확인
        assertThat(cachedUserRepository.existsByKey(userId)).isTrue();
    }

    @Test
    @DisplayName("사용자가 존재하지 않으면 예외가 발생한다")
    void checkUserExistsOrThrow_fail_whenUserNotFound_throwsUserNotFound() {
        // given
        Long userId = 1L;

        // when & then
        assertThatThrownBy(() -> userValidator.checkUserExistsOrThrow(userId))
            .isInstanceOf(UserNotFound.class);
    }

    @Test
    @DisplayName("사용자가 존재하면 true를 반환하고 캐시에 저장한다")
    void isUserExists_success_whenUserExists_returnsTrueAndCaches() {
        // given
        Long userId = 1L;
        userRepository.save(User.create(userId, "user@example.com", "nick", LocalDateTime.now()));

        // when
        boolean result = userValidator.isUserExists(userId);

        // then
        assertThat(result).isTrue();
        assertThat(cachedUserRepository.existsByKey(userId)).isTrue();
    }

    @Test
    @DisplayName("사용자가 존재하지 않으면 false를 반환한다")
    void isUserExists_false_whenUserNotExists_returnsFalse() {
        // given
        Long userId = 1L;

        // when
        boolean result = userValidator.isUserExists(userId);

        // then
        assertThat(result).isFalse();
        assertThat(cachedUserRepository.existsByKey(userId)).isFalse();
    }

}