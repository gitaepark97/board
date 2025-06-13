package board.backend.auth.infra.jpa;

import board.backend.auth.domain.LoginMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface LoginInfoEntityRepository extends JpaRepository<LoginInfoEntity, Long> {

    Optional<LoginInfoEntity> findByLoginMethodAndLoginKey(LoginMethod loginMethod, String loginKey);

}
