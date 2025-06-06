package board.backend.user.application;

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

    private UserRepository userRepository;
    private UserReader userReader;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userReader = new UserReader(userRepository);
    }

    @Test
    @DisplayName("회원이 존재하면 예외가 발생하지 않는다")
    void checkUserExists_success() {
        // given
        Long userId = 1L;
        when(userRepository.customExistsById(userId)).thenReturn(true);

        // when
        userReader.checkUserExists(userId);

        // then
    }

    @Test
    @DisplayName("회원이 존재하지 않으면 예외가 발생한다")
    void checkUserExists_fail() {
        // given
        Long userId = 1L;
        when(userRepository.customExistsById(userId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> userReader.checkUserExists(userId))
            .isInstanceOf(UserNotFound.class);
    }

    @Test
    @DisplayName("회원 존재 여부를 반환한다")
    void isUserExists() {
        // given
        Long userId = 1L;
        when(userRepository.customExistsById(userId)).thenReturn(true);

        // when
        boolean result = userReader.isUserExists(userId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("이메일로 회원을 조회한다")
    void read_byEmail() {
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
    @DisplayName("회원 ID로 회원을 조회한다")
    void read_byId_success() {
        // given
        Long userId = 1L;
        User user = User.create(userId, "email@test.com", "nickname", LocalDateTime.now());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        User result = userReader.read(userId);

        // then
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("회원 ID로 조회 시 존재하지 않으면 예외가 발생한다")
    void read_byId_fail() {
        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userReader.read(userId))
            .isInstanceOf(UserNotFound.class);
    }

    @Test
    @DisplayName("여러 회원 ID로 회원 목록을 조회한다")
    void readAll_success() {
        // given
        List<Long> userIds = List.of(1L, 2L);
        User user1 = User.create(1L, "user1@email.com", "user1", LocalDateTime.now());
        User user2 = User.create(2L, "user2@email.com", "user2", LocalDateTime.now());
        when(userRepository.findAllById(userIds)).thenReturn(List.of(user1, user2));

        // when
        Map<Long, User> result = userReader.readAll(userIds);

        // then
        assertThat(result).containsEntry(1L, user1).containsEntry(2L, user2);
    }

}