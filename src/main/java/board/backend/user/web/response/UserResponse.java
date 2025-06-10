package board.backend.user.web.response;

import board.backend.user.domain.User;

import java.time.LocalDateTime;

public record UserResponse(
    String id,
    String email,
    String nickname,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId().toString(),
            user.getEmail(),
            user.getNickname(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }

}
