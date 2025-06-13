package board.backend.auth.application.port;

import board.backend.auth.domain.LoginInfo;
import board.backend.auth.domain.LoginMethod;

import java.util.Optional;

public interface LoginInfoRepository {

    boolean existsBy(LoginMethod loginMethod, String loginKey);

    Optional<LoginInfo> findByLoginMethodAndLoginKey(LoginMethod loginMethod, String loginKey);

    void save(LoginInfo loginInfo);

}
