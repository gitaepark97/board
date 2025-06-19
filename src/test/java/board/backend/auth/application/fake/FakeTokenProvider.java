package board.backend.auth.application.fake;


import board.backend.auth.application.port.TokenProvider;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class FakeTokenProvider implements TokenProvider {

    private final Map<String, Map<String, Object>> store = new HashMap<>();
    private long sequence = 1;

    @Override
    public String issueToken(Map<String, Object> claims, Duration duration) {
        String token = "token-" + sequence++;
        store.put(token, claims);
        return token;
    }

    @Override
    public Map<String, Object> getPayload(String token) {
        if (!store.containsKey(token)) {
            throw new IllegalArgumentException("Invalid token");
        }
        return store.get(token);
    }

    public void addPayload(String token, Map<String, Object> payload) {
        store.put(token, payload);
    }

}
