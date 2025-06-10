package board.backend.auth.infra;

import board.backend.auth.domain.Session;

import java.util.Optional;

public interface SessionRepository {

    Optional<Session> findById(String id);

    void save(Session session);

    void deleteByUserId(Long userId);

}
