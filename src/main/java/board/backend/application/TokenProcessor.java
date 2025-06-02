package board.backend.application;

import board.backend.application.dto.Token;
import board.backend.domain.Session;
import board.backend.infra.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class TokenProcessor {

    private static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(1);

    private final TokenProvider tokenProvider;

    Token issueToken(Session session) {
        return new Token(
            // access token 발급
            tokenProvider.issueToken(Map.of("userId", session.getUserId().toString()), ACCESS_TOKEN_DURATION),
            // 세션 ID 사용
            session.getId()
        );
    }

    Optional<Long> getUserId(String token) {
        try {
            Map<String, Object> payload = tokenProvider.getPayload(token);
            return Optional.of(Long.parseLong(payload.get("userId").toString()));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

}
