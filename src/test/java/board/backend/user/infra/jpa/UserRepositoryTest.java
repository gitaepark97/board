package board.backend.user.infra.jpa;

import board.backend.common.infra.TestRepository;
import board.backend.user.application.port.UserRepository;
import board.backend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@Import(UserRepositoryImpl.class)
class UserRepositoryTest extends TestRepository {

    private final Long userId = 1L;
    private final String email = "user1@example.com";
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = User.create(userId, email, "닉네임1", LocalDateTime.now());
        userRepository.save(user);
    }

    @Test
    @DisplayName("userId로 사용자가 존재하면 true를 반환한다")
    void existsById_success_whenExists_returnsTrue() {
        // when
        boolean result = userRepository.existsById(userId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("userId로 사용자가 존재하지 않으면 false를 반환한다")
    void existsById_success_whenNotExists_returnsFalse() {
        // when
        boolean result = userRepository.existsById(999L);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("email로 사용자가 존재하면 true를 반환한다")
    void existsByEmail_success_whenExists_returnsTrue() {
        // when
        boolean result = userRepository.existsByEmail(email);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("email로 사용자가 존재하지 않으면 false를 반환한다")
    void existsByEmail_success_whenNotExists_returnsFalse() {
        // when
        boolean result = userRepository.existsByEmail("user999@example.com");

        // then
        assertThat(result).isFalse();
    }

}