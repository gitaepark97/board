package board.backend.user.application;

import board.backend.common.infra.CacheRepository;
import board.backend.user.domain.User;
import board.backend.user.domain.UserNotFound;
import board.backend.user.infra.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserReaderTest {

    private CacheRepository<User, Long> userCacheRepository;
    private UserRepository userRepository;
    private UserReader userReader;

    @BeforeEach
    void setUp() {
        userCacheRepository = mock(CacheRepository.class);
        userRepository = mock(UserRepository.class);
        userReader = new UserReader(userCacheRepository, userRepository);
    }

    @Test
    @DisplayName("회원이 캐시에 존재하면 예외 없이 통과한다")
    void checkUserExists_whenCacheHit_shouldPass() {
        // given
        Long userId = 1L;
        when(userCacheRepository.get(userId)).thenReturn(Optional.of(mock(User.class)));

        // when
        userReader.checkUserExists(userId);
    }

    @Test
    @DisplayName("회원이 캐시에 없지만 DB에 존재하면 예외가 발생하지 않는다")
    void checkUserExists_successByDB() {
        // given
        Long userId = 1L;
        when(userCacheRepository.get(userId)).thenReturn(Optional.empty());
        when(userRepository.customExistsById(userId)).thenReturn(true);

        // when
        userReader.checkUserExists(userId);
    }

    @Test
    @DisplayName("회원이 캐시에 없고 DB에는 존재하면 예외 없이 통과한다")
    void checkUserExists_whenCacheMissAndDbHit_shouldPass() {
        // given
        Long userId = 1L;
        when(userCacheRepository.get(userId)).thenReturn(Optional.empty());
        when(userRepository.customExistsById(userId)).thenReturn(true);

        // when
        userReader.checkUserExists(userId);
    }

    @Test
    @DisplayName("회원이 캐시와 DB에 모두 없으면 예외가 발생한다")
    void checkUserExists_whenCacheAndDbMiss_shouldThrow() {
        // given
        Long userId = 1L;
        when(userCacheRepository.get(userId)).thenReturn(Optional.empty());
        when(userRepository.customExistsById(userId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> userReader.checkUserExists(userId))
            .isInstanceOf(UserNotFound.class);
    }

    @Test
    @DisplayName("회원 존재 여부를 캐시에서 조회해 true를 반환한다")
    void isUserExists_whenCacheHit_shouldReturnTrue() {
        // given
        Long userId = 1L;
        when(userCacheRepository.get(userId)).thenReturn(Optional.of(mock(User.class)));

        // when
        boolean result = userReader.isUserExists(userId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("회원 존재 여부를 DB에서 조회해 true를 반환한다")
    void isUserExists_whenDbHit_shouldReturnTrue() {
        // given
        Long userId = 1L;
        when(userCacheRepository.get(userId)).thenReturn(Optional.empty());
        when(userRepository.customExistsById(userId)).thenReturn(true);

        // when
        boolean result = userReader.isUserExists(userId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("이메일로 회원을 조회해 Optional로 반환한다")
    void read_whenEmailExists_shouldReturnUser() {
        // given
        String email = "test@email.com";
        User user = User.create(1L, email, "nickname", LocalDateTime.now());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        Optional<User> result = userReader.read(email);

        // then
        assertThat(result).contains(user);
    }

    @Test
    @DisplayName("회원 ID로 캐시에서 조회해 반환한다")
    void read_whenCacheHit_shouldReturnUser() {
        // given
        Long userId = 1L;
        User user = User.create(userId, "email@test.com", "nickname", LocalDateTime.now());
        when(userCacheRepository.get(userId)).thenReturn(Optional.of(user));

        // when
        User result = userReader.read(userId);

        // then
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("회원 ID로 캐시에 없을 때 DB에서 조회해 반환한다")
    void read_whenCacheMiss_shouldReturnUserFromDb() {
        // given
        Long userId = 1L;
        User user = User.create(userId, "email@test.com", "nickname", LocalDateTime.now());
        when(userCacheRepository.get(userId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        User result = userReader.read(userId);

        // then
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("회원 ID로 조회 시 캐시와 DB 모두 없으면 예외가 발생한다")
    void read_whenCacheAndDbMiss_shouldThrow() {
        // given
        Long userId = 1L;
        when(userCacheRepository.get(userId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userReader.read(userId))
            .isInstanceOf(UserNotFound.class);
    }

    @Test
    @DisplayName("여러 회원 ID 조회 시 캐시에 모두 존재하면 DB 조회 없이 반환한다")
    void readAll_whenAllCacheHit_shouldReturnFromCacheOnly() {
        // given
        Long user1Id = 1L;
        Long user2Id = 2L;
        User user1 = User.create(user1Id, "user1@email.com", "user1", LocalDateTime.now());
        User user2 = User.create(user2Id, "user2@email.com", "user2", LocalDateTime.now());

        List<User> cachedUsers = List.of(user1, user2);
        when(userCacheRepository.getAll(List.of(user1Id, user2Id))).thenReturn(cachedUsers);

        // when
        Map<Long, User> result = userReader.readAll(List.of(user1Id, user2Id));

        // then
        assertThat(result).containsEntry(user1Id, user1).containsEntry(user2Id, user2);
    }

    @Test
    @DisplayName("여러 회원 ID 조회 시 캐시에 모두 없으면 DB에서 모두 조회하고 반환한다")
    void readAll_whenAllCacheMiss_shouldReturnFromDb() {
        // given
        Long user1Id = 1L;
        Long user2Id = 2L;
        User user1 = User.create(user1Id, "user1@email.com", "user1", LocalDateTime.now());
        User user2 = User.create(user2Id, "user2@email.com", "user2", LocalDateTime.now());

        when(userCacheRepository.getAll(List.of(user1Id, user2Id))).thenReturn(List.of());
        when(userRepository.findAllById(List.of(user1Id, user2Id))).thenReturn(List.of(user1, user2));

        // when
        Map<Long, User> result = userReader.readAll(List.of(user1Id, user2Id));

        // then
        assertThat(result).containsEntry(user1Id, user1).containsEntry(user2Id, user2);
    }

    @Test
    @DisplayName("여러 회원 ID로 조회할 때 일부 캐시 hit이면 나머지를 DB에서 조회해 합쳐 반환한다")
    void readAll_whenPartialCacheHit_shouldReturnMerged() {
        // given
        Long user1Id = 1L;
        Long user2Id = 2L;
        User user1 = User.create(user1Id, "user1@email.com", "user1", LocalDateTime.now());
        User user2 = User.create(user2Id, "user2@email.com", "user2", LocalDateTime.now());

        when(userCacheRepository.getAll(List.of(user1Id, user2Id))).thenReturn(List.of(user1));
        when(userRepository.findAllById(List.of(user2Id))).thenReturn(List.of(user2));

        // when
        Map<Long, User> result = userReader.readAll(List.of(user1Id, user2Id));

        // then
        assertThat(result).containsEntry(user1Id, user1).containsEntry(user2Id, user2);
    }

}