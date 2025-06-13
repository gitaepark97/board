package board.backend.auth.infra.redis;

import board.backend.auth.application.port.SessionRepository;
import board.backend.auth.domain.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
class SessionRepositoryImpl implements SessionRepository {

    private final SessionEntityRepository sessionEntityRepository;

    @Override
    public Optional<Session> findById(String id) {
        return sessionEntityRepository.findById(id).map(SessionEntity::toSession);
    }

    @Override
    public void save(Session session) {
        sessionEntityRepository.save(SessionEntity.from(session));
    }

    @Override
    public void deleteByUserId(Long userId) {
        sessionEntityRepository.deleteByUserId(userId);
    }

}
