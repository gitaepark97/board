package board.backend.application;

import board.backend.domain.User;
import board.backend.domain.UserEmailDuplicated;
import board.backend.domain.UserNotFound;
import board.backend.infra.UserRepository;
import board.backend.support.IdProvider;
import board.backend.support.TimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserWriterTest {

    private IdProvider idProvider;
    private TimeProvider timeProvider;
    private UserRepository userRepository;
    private UserWriter userWriter;

    @BeforeEach
    void setUp() {
        idProvider = mock(IdProvider.class);
        timeProvider = mock(TimeProvider.class);
        userRepository = mock(UserRepository.class);
        userWriter = new UserWriter(idProvider, timeProvider, userRepository);
    }

    @Test
    @DisplayName("중복된 이메일이면 예외를 던진다")
    void create_duplicatedEmail_throwsException() {
        // given
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> userWriter.create(email, "nickname"))
            .isInstanceOf(UserEmailDuplicated.class);
    }

    @Test
    @DisplayName("중복되지 않은 이메일이면 유저 생성에 성공한다")
    void create_success() {
        // given
        String email = "test@example.com";
        String nickname = "tester";
        Long generatedId = 1L;
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(idProvider.nextId()).thenReturn(generatedId);
        when(timeProvider.now()).thenReturn(now);

        // when
        User result = userWriter.create(email, nickname);

        // then
        assertThat(result.getId()).isEqualTo(generatedId);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getNickname()).isEqualTo(nickname);
        assertThat(result.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("존재하지 않는 유저 ID로 수정하면 예외가 발생한다")
    void update_userNotFound_throwsException() {
        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userWriter.update(userId, "new-nick"))
            .isInstanceOf(UserNotFound.class);
    }

    @Test
    @DisplayName("유저 닉네임 수정에 성공한다")
    void update_success() {
        // given
        Long userId = 1L;
        String oldNickname = "old";
        String newNickname = "new";
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 13, 0);

        User original = User.create(userId, "email@example.com", oldNickname, LocalDateTime.of(2023, 12, 31, 0, 0));
        User updated = original.update(newNickname, now);

        when(userRepository.findById(userId)).thenReturn(Optional.of(original));
        when(timeProvider.now()).thenReturn(now);

        // when
        User result = userWriter.update(userId, newNickname);

        // then
        assertThat(result.getNickname()).isEqualTo(newNickname);
        assertThat(result.getUpdatedAt()).isEqualTo(now);
    }

}