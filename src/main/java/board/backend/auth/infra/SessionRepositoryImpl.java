package board.backend.auth.infra;

import board.backend.auth.domain.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
class SessionRepositoryImpl implements SessionRepository {

    private static final String SESSION_KEY_FORMAT = "session::%s";
    private static final String USER_KEY_FORMAT = "session::user::%s";

    private final RedisTemplate<String, Object> sessions;
    private final StringRedisTemplate sessionUsers;

    @Override
    public Optional<Session> findById(String id) {
        String sessionKey = generateSessionKey(id);
        Object value = sessions.opsForValue().get(sessionKey);
        return value instanceof Session ? Optional.of((Session) value) : Optional.empty();
    }

    @Override
    public void save(Session session) {
        String sessionKey = generateSessionKey(session.id());
        String userIndexKey = generateUserIndexKey(session.userId());

        sessions.opsForValue().set(sessionKey, session, Session.SESSION_DURATION);
        sessionUsers.opsForValue().set(userIndexKey, session.id(), Session.SESSION_DURATION);
    }

    @Override
    public void deleteByUserId(Long userId) {
        String userIndexKey = generateUserIndexKey(userId);
        String sessionId = sessionUsers.opsForValue().get(userIndexKey);

        sessions.delete(generateSessionKey(sessionId));
        sessions.delete(userIndexKey);
    }

    private String generateSessionKey(String id) {
        return String.format(SESSION_KEY_FORMAT, id);
    }

    private String generateUserIndexKey(Long id) {
        return String.format(USER_KEY_FORMAT, id);
    }

}
