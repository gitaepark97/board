package board.backend.auth.application.fake;

import board.backend.auth.application.port.SessionRepository;
import board.backend.auth.domain.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeSessionRepository implements SessionRepository {

    private final Map<String, Session> store = new HashMap<>();

    @Override
    public Optional<Session> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void save(Session session) {
        store.put(session.id(), session);
    }

    @Override
    public void deleteByUserId(Long userId) {
        store.values().removeIf(session -> session.userId().equals(userId));
    }

}
