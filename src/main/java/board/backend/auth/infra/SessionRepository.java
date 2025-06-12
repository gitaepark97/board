package board.backend.auth.infra;

import board.backend.auth.domain.Session;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends KeyValueRepository<Session, String> {

    void deleteByUserId(Long userId);

}
