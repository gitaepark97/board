package board.backend.application;

import board.backend.domain.User;
import board.backend.domain.UserNotFound;
import board.backend.infra.UserRepository;
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
    @DisplayName("이메일로 유저 조회에 성공한다")
    void read_byEmailSuccess() {
        // given
        String email = "test@example.com";
        User user = User.create(1L, email, "닉네임", LocalDateTime.now()); // 생성자 혹은 builder 방식에 맞게 수정

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        Optional<User> result = userReader.read(email);

        // then
        assertThat(result).contains(user);
    }

    @Test
    @DisplayName("존재하지 않는 회원이면 빈 Optional을 반환한다")
    void read_byEmailNotFound() {
        // given
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when
        Optional<User> result = userReader.read(email);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("ID로 유저 조회에 성공한다")
    void read_byUserIdSuccess() {
        // given
        Long userId = 1L;
        User user = User.create(userId, "email@example.com", "닉네임", LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        User result = userReader.read(userId);

        // then
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("존재하지 않는 회원이면 예외가 발생한다")
    void read_byIdNotFound() {
        // given
        Long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userReader.read(userId))
            .isInstanceOf(UserNotFound.class);
    }

    @Test
    @DisplayName("사용자가 존재하면 true를 반환한다")
    void isUserExists_true() {
        // given
        Long userId = 1L;
        when(userRepository.customExistsById(userId)).thenReturn(true);

        // when
        boolean result = userReader.isUserExists(userId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("사용자가 존재하지 않으면 false를 반환한다")
    void isUserExists_false() {
        // given
        Long userId = 2L;
        when(userRepository.customExistsById(userId)).thenReturn(false);

        // when
        boolean result = userReader.isUserExists(userId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("여러 유저 ID로 유저 목록을 조회해 Map으로 반환한다")
    void readAll_success() {
        // given
        List<Long> userIds = List.of(1L, 2L);
        List<User> users = List.of(
            User.create(1L, "one@example.com", "one", LocalDateTime.now()),
            User.create(2L, "two@example.com", "two", LocalDateTime.now())
        );

        when(userRepository.findAllById(userIds)).thenReturn(users);

        // when
        Map<Long, User> result = userReader.readAll(userIds);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(1L)).isEqualTo(users.get(0));
        assertThat(result.get(2L)).isEqualTo(users.get(1));
    }

    @Test
    @DisplayName("조회된 유저가 없으면 빈 Map을 반환한다")
    void readAll_emptyResult() {
        // given
        List<Long> userIds = List.of(100L, 200L);
        when(userRepository.findAllById(userIds)).thenReturn(List.of());

        // when
        Map<Long, User> result = userReader.readAll(userIds);

        // then
        assertThat(result).isEmpty();
    }

}