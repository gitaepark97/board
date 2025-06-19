package board.backend.user.infra.jpa;

import board.backend.common.infra.TestJpaRepository;
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
class UserRepositoryTest extends TestJpaRepository {

    private final User user = User.create(1L, "user1@example.com", "닉네임1", LocalDateTime.now());

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("userId로 사용자가 존재하면 true를 반환한다")
    void existsById_success_whenExists_returnsTrue() {
        // given
        userRepository.save(user);

        // when
        boolean result = userRepository.existsById(user.id());

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
        // given
        userRepository.save(user);

        // when
        boolean result = userRepository.existsByEmail(user.email());

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