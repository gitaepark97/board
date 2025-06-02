package board.backend.infra;

import board.backend.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {

    void deleteByUserId(Long userId);

}
