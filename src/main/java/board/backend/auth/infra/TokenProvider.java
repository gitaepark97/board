package board.backend.auth.infra;

import java.time.Duration;
import java.util.Map;

public interface TokenProvider {

    String issueToken(Map<String, Object> claims, Duration duration);

    Map<String, Object> getPayload(String token);

}
