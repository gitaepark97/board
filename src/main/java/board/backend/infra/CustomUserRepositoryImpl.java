package board.backend.infra;

import board.backend.domain.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean customExistsById(Long id) {
        QUser user = QUser.user;

        Integer result = queryFactory
            .selectOne()
            .from(user)
            .where(user.id.eq(id))
            .fetchFirst();

        return result != null;
    }

    @Override
    public boolean existsByEmail(String email) {
        QUser user = QUser.user;

        Integer result = queryFactory
            .selectOne()
            .from(user)
            .where(user.email.eq(email))
            .fetchFirst();

        return result != null;
    }

}
