package board.backend.infra;

import board.backend.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Import(CustomUserRepositoryImpl.class)
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
    @DisplayName("ID로 회원 존재 여부를 확인한다 - 존재함")
    void customExistsById_exists() {
        // when
        boolean result = userRepository.customExistsById(userId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("ID로 회원 존재 여부를 확인한다 - 존재하지 않음")
    void customExistsById_notExists() {
        // when
        boolean result = userRepository.customExistsById(999L);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("email로 회원 존재 여부를 확인한다 - 존재함")
    void existsByEmail_exists() {
        // when
        boolean result = userRepository.existsByEmail(email);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("email로 회원 존재 여부를 확인한다 - 존재하지 않음")
    void existsByEmail_notExists() {
        // when
        boolean result = userRepository.existsByEmail("user999@example.com");

        // then
        assertThat(result).isFalse();
    }

}