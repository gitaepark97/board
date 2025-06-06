package board.backend.auth.infra;

import board.backend.auth.domain.LoginMethod;

interface CustomLoginInfoRepository {

    boolean existsByLoginMethodAndLoginKey(LoginMethod loginMethod, String loginKey);

}
