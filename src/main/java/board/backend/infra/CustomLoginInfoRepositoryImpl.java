package board.backend.infra;

import board.backend.domain.LoginMethod;
import board.backend.domain.QLoginInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class CustomLoginInfoRepositoryImpl implements CustomLoginInfoRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByLoginMethodAndLoginKey(LoginMethod loginMethod, String loginKey) {
        QLoginInfo loginInfo = QLoginInfo.loginInfo;

        Integer result = queryFactory
            .selectOne()
            .from(loginInfo)
            .where(
                loginInfo.loginMethod.eq(loginMethod),
                loginInfo.loginKey.eq(loginKey)
            )
            .fetchFirst();

        return result != null;
    }

}
