package board.backend.web.response;

import board.backend.application.dto.Token;

public record TokenResponse(
    String accessToken
) {

    public static TokenResponse from(Token token) {
        return new TokenResponse(token.access());
    }

}
