package board.backend.auth.infra;

import board.backend.auth.domain.LoginInfo;
import board.backend.auth.domain.LoginMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long>, CustomLoginInfoRepository {

    Optional<LoginInfo> findByLoginMethodAndLoginKey(LoginMethod loginMethod, String loginKey);

}
