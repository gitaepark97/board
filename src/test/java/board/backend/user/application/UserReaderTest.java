package board.backend.user.application;

import board.backend.common.cache.fake.FakeCachedRepository;
import board.backend.user.application.fake.FakeUserRepository;
import board.backend.user.domain.User;
import board.backend.user.domain.UserNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserReaderTest {

    private FakeCachedRepository<User, Long> cachedRepository;
    private FakeUserRepository userRepository;
    private UserReader userReader;

    @BeforeEach
    void setUp() {
        cachedRepository = new FakeCachedRepository<>();
        userRepository = new FakeUserRepository();
        userReader = new UserReader(cachedRepository, userRepository);
    }

    @Test
    @DisplayName("이메일로 사용자를 조회할 수 있다")
    void read_success_whenEmailExists_returnsUser() {
        // given
        String email = "test@example.com";
        User user = User.create(1L, email, "tester", LocalDateTime.now());
        userRepository.save(user);

        // when
        Optional<User> result = userReader.read(email);

        // then
        assertThat(result).contains(user);
    }

    @Test
    @DisplayName("ID로 사용자를 조회할 수 있다 (캐시에 없음)")
    void read_success_whenUserNotInCache_returnsFromRepositoryAndCaches() {
        // given
        Long id = 1L;
        User user = User.create(id, "test@example.com", "tester", LocalDateTime.now());
        userRepository.save(user);

        // when
        User result = userReader.read(id);

        // then
        assertThat(result).isEqualTo(user);
        assertThat(cachedRepository.findByKey(id)).contains(user);
    }

    @Test
    @DisplayName("ID로 사용자를 조회할 수 있다 (캐시에 있음)")
    void read_success_whenUserInCache_returnsFromCache() {
        // given
        Long id = 1L;
        User user = User.create(id, "test@example.com", "tester", LocalDateTime.now());
        cachedRepository.save(id, user, Duration.ofMinutes(10));

        // when
        User result = userReader.read(id);

        // then
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("ID로 사용자를 조회할 때 존재하지 않으면 예외가 발생한다")
    void read_fail_whenUserNotFound_throwsUserNotFound() {
        // given
        Long id = 1L;

        // when & then
        assertThatThrownBy(() -> userReader.read(id))
            .isInstanceOf(UserNotFound.class);
    }

    @Test
    @DisplayName("ID 목록으로 여러 사용자를 조회할 수 있다 (캐시 미스 포함)")
    void readAll_success_whenSomeUsersCached_returnsAllAndCachesMissed() {
        // given
        User user1 = User.create(1L, "a@example.com", "a", LocalDateTime.now());
        User user2 = User.create(2L, "b@example.com", "b", LocalDateTime.now());
        User user3 = User.create(3L, "c@example.com", "c", LocalDateTime.now());

        cachedRepository.save(1L, user1, Duration.ofMinutes(10));
        userRepository.save(user2);
        userRepository.save(user3);

        // when
        Map<Long, User> result = userReader.readAll(List.of(1L, 2L, 3L));

        // then
        assertThat(result).containsEntry(1L, user1);
        assertThat(result).containsEntry(2L, user2);
        assertThat(result).containsEntry(3L, user3);

        // and: uncached user들이 캐시에 저장되었는지 확인
        assertThat(cachedRepository.findByKey(2L)).contains(user2);
        assertThat(cachedRepository.findByKey(3L)).contains(user3);
    }

}