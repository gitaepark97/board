package board.backend.application;

import board.backend.domain.User;
import board.backend.infra.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    void read_success() {
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
    @DisplayName("존재하지 않는 이메일이면 빈 Optional을 반환한다")
    void read_notFound() {
        // given
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when
        Optional<User> result = userReader.read(email);

        // then
        assertThat(result).isEmpty();
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


}