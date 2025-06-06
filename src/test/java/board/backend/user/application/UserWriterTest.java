package board.backend.user.application;

import board.backend.common.support.IdProvider;
import board.backend.common.support.TimeProvider;
import board.backend.user.domain.User;
import board.backend.user.domain.UserEmailDuplicated;
import board.backend.user.domain.UserNotFound;
import board.backend.user.infra.UserRepository;
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
    @DisplayName("회원 생성에 성공한다")
    void create_success() {
        // given
        String email = "user@example.com";
        String nickname = "nickname";
        LocalDateTime now = LocalDateTime.now();
        Long id = 1L;

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(idProvider.nextId()).thenReturn(id);
        when(timeProvider.now()).thenReturn(now);

        // when
        User result = userWriter.create(email, nickname);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getNickname()).isEqualTo(nickname);
    }

    @Test
    @DisplayName("이메일이 중복되면 예외가 발생한다")
    void create_failWhenEmailDuplicated() {
        // given
        String email = "user@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> userWriter.create(email, "nickname"))
            .isInstanceOf(UserEmailDuplicated.class);
    }

    @Test
    @DisplayName("회원 수정에 성공한다")
    void update_success() {
        // given
        Long userId = 1L;
        String nickname = "newNickname";
        LocalDateTime now = LocalDateTime.now();
        User user = User.create(userId, "user@example.com", "oldNickname", now.minusDays(1));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(timeProvider.now()).thenReturn(now);

        // when
        User result = userWriter.update(userId, nickname);

        // then
        assertThat(result.getNickname()).isEqualTo(nickname);
    }

    @Test
    @DisplayName("회원 수정 시 존재하지 않으면 예외가 발생한다")
    void update_failWhenNotFound() {
        // given
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userWriter.update(userId, "nickname"))
            .isInstanceOf(UserNotFound.class);
    }

}