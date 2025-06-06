package board.backend.user.web.response;

import board.backend.user.domain.User;

public record UserSummaryResponse(
    Long id,
    String nickname
) {

    public static UserSummaryResponse from(User user) {
        return new UserSummaryResponse(user.getId(), user.getNickname());
    }

}
