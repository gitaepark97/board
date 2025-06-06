package board.backend.auth.infra;

import board.backend.auth.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {

    void deleteByUserId(Long userId);

}
