package board.backend.auth.application.fake;

import board.backend.auth.application.port.LoginInfoRepository;
import board.backend.auth.domain.LoginInfo;
import board.backend.auth.domain.LoginMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeLoginInfoRepository implements LoginInfoRepository {

    private final Map<Key, LoginInfo> store = new HashMap<>();

    @Override
    public boolean existsBy(LoginMethod loginMethod, String loginKey) {
        return store.containsKey(new Key(loginMethod, loginKey));
    }

    @Override
    public Optional<LoginInfo> findByLoginMethodAndLoginKey(LoginMethod loginMethod, String loginKey) {
        return Optional.ofNullable(store.get(new Key(loginMethod, loginKey)));
    }

    @Override
    public void save(LoginInfo loginInfo) {
        store.put(new Key(loginInfo.loginMethod(), loginInfo.loginKey()), loginInfo);
    }

    private record Key(LoginMethod method, String key) {

    }

}
