package board.backend.infra;

import board.backend.TestcontainersConfiguration;
import board.backend.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@Import({
    TestcontainersConfiguration.class,
    QueryDSLConfig.class,
    CustomUserRepositoryImpl.class
})
@DataJpaTest
class UserRepositoryTest {

    private final String email = "user1@example.com";
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = User.create(1L, email, "닉네임1", LocalDateTime.now());
        userRepository.save(user);
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