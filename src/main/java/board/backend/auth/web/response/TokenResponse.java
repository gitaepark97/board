package board.backend.auth.web.response;

import board.backend.auth.application.dto.Token;

public record TokenResponse(
    String accessToken
) {

    public static TokenResponse from(Token token) {
        return new TokenResponse(token.access());
    }

}
