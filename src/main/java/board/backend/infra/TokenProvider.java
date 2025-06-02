package board.backend.infra;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class TokenProvider {

    private final SecretKey secretKey;

    public TokenProvider(@Value("${jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key()
            .build()
            .getAlgorithm());
    }

    public String issueToken(Map<String, Object> claims, Duration duration) {
        Instant now = Instant.now();
        Instant expiredAt = now.plus(duration);

        return Jwts.builder()
            .claims(claims)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiredAt))
            .signWith(secretKey)
            .compact();
    }

}
