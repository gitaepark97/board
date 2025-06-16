package board.backend.user.web.response;

import board.backend.user.domain.User;
import org.springframework.modulith.NamedInterface;

@NamedInterface
public record UserResponse(
    String id,
    String nickname
) {

    public static UserResponse from(User user) {
        return new UserResponse(user.id().toString(), user.nickname());
    }

}
