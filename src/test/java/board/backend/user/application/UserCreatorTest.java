package board.backend.user.application;

import board.backend.common.support.fake.FakeIdProvider;
import board.backend.common.support.fake.FakeTimeProvider;
import board.backend.user.application.fake.FakeUserRepository;
import board.backend.user.domain.User;
import board.backend.user.domain.UserEmailDuplicated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class UserCreatorTest {

    private final Long id = 1L;
    private final LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

    private FakeUserRepository userRepository;
    private UserCreator userCreator;

    @BeforeEach
    void setUp() {
        FakeIdProvider idProvider = new FakeIdProvider(id);
        FakeTimeProvider timeProvider = new FakeTimeProvider(now);
        userRepository = new FakeUserRepository();
        userCreator = new UserCreator(idProvider, timeProvider, userRepository);
    }

    @Test
    @DisplayName("새로운 사용자를 생성할 수 있다")
    void create_success_whenEmailIsUnique_returnsNewUser() {
        // given
        String email = "test@example.com";
        String nickname = "tester";

        // when
        User createdUser = userCreator.create(email, nickname);

        // then
        assertThat(createdUser.id()).isEqualTo(id);
        assertThat(createdUser.email()).isEqualTo(email);
        assertThat(createdUser.nickname()).isEqualTo(nickname);
        assertThat(createdUser.createdAt()).isEqualTo(now);
        assertThat(userRepository.findById(id)).contains(createdUser);
    }

    @Test
    @DisplayName("이메일이 중복되면 예외가 발생한다")
    void create_fail_whenEmailAlreadyExists_throwsUserEmailDuplicated() {
        // given
        String email = "duplicate@example.com";

        // 이미 등록된 사용자
        User existing = User.create(id, email, "existing", LocalDateTime.now());
        userRepository.save(existing);

        // when & then
        assertThatThrownBy(() -> userCreator.create(email, "newnick"))
            .isInstanceOf(UserEmailDuplicated.class);
    }

}