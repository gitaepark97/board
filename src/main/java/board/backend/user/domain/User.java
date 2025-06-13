package board.backend.user.domain;

import lombok.Builder;
import org.springframework.modulith.NamedInterface;

import java.time.LocalDateTime;

@NamedInterface
@Builder(toBuilder = true)
public record User(
    Long id,
    String email,
    String nickname,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static User create(Long id, String email, String nickname, LocalDateTime now) {
        return User.builder()
            .id(id)
            .email(email)
            .nickname(nickname)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    public User update(String nickname, LocalDateTime now) {
        return toBuilder()
            .nickname(nickname)
            .updatedAt(now)
            .build();
    }

}
