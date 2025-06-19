package board.backend.auth.application;

import board.backend.auth.application.dto.Token;
import board.backend.auth.application.port.TokenProvider;
import board.backend.auth.domain.Session;
import board.backend.user.application.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@NamedInterface
@RequiredArgsConstructor
@Component
public class TokenManager {

    private static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(1);

    private final TokenProvider tokenProvider;
    private final UserValidator userValidator;

    Token issueToken(Session session) {
        return new Token(
            // access token 발급
            tokenProvider.issueToken(Map.of("userId", session.userId().toString()), ACCESS_TOKEN_DURATION),
            // 세션 ID 사용
            session.id()
        );
    }

    public Optional<Long> getUserId(String token) {
        try {
            Map<String, Object> payload = tokenProvider.getPayload(token);
            return Optional.of(Long.parseLong(payload.get("userId").toString())).filter(userValidator::isUserExists);
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

}
