package board.backend.user.web.response;

import board.backend.user.domain.User;

public record UserSummaryResponse(
    String id,
    String nickname
) {

    public static UserSummaryResponse from(User user) {
        return new UserSummaryResponse(user.getId().toString(), user.getNickname());
    }

}
