package board.backend.auth.infra.jpa;

import board.backend.auth.application.port.LoginInfoRepository;
import board.backend.auth.domain.LoginInfo;
import board.backend.auth.domain.LoginMethod;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
class LoginInfoRepositoryImpl implements LoginInfoRepository {

    private final JPAQueryFactory queryFactory;
    private final LoginInfoEntityRepository loginInfoEntityRepository;

    @Override
    public boolean existsBy(LoginMethod loginMethod, String loginKey) {
        QLoginInfoEntity loginInfoEntity = QLoginInfoEntity.loginInfoEntity;

        Integer result = queryFactory
            .selectOne()
            .from(loginInfoEntity)
            .where(
                loginInfoEntity.loginMethod.eq(loginMethod),
                loginInfoEntity.loginKey.eq(loginKey)
            )
            .fetchFirst();

        return result != null;
    }

    @Override
    public Optional<LoginInfo> findByLoginMethodAndLoginKey(LoginMethod loginMethod, String loginKey) {
        return loginInfoEntityRepository.findByLoginMethodAndLoginKey(loginMethod, loginKey)
            .map(LoginInfoEntity::toLoginInfo);
    }

    @Override
    public void save(LoginInfo loginInfo) {
        loginInfoEntityRepository.save(LoginInfoEntity.from(loginInfo));
    }

}
