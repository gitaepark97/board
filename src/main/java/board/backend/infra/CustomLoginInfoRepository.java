package board.backend.infra;

import board.backend.domain.LoginMethod;

interface CustomLoginInfoRepository {

    boolean existsByLoginMethodAndLoginKey(LoginMethod loginMethod, String loginKey);

}
