package board.backend.user.web.response;

import board.backend.user.domain.User;

import java.time.LocalDateTime;

public record MyUserResponse(
    String id,
    String email,
    String nickname,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static MyUserResponse from(User user) {
        return new MyUserResponse(
            user.id().toString(),
            user.email(),
            user.nickname(),
            user.createdAt(),
            user.updatedAt()
        );
    }

}
