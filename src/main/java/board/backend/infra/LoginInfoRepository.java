package board.backend.infra;

import board.backend.domain.LoginInfo;
import board.backend.domain.LoginMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long>, CustomLoginInfoRepository {

    Optional<LoginInfo> findByLoginMethodAndUserId(LoginMethod loginMethod, Long userId);

    Optional<LoginInfo> findByLoginMethodAndLoginKey(LoginMethod loginMethod, String loginKey);

}
