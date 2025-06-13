package board.backend.user.infra.jpa;

import board.backend.user.application.port.UserRepository;
import board.backend.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class UserRepositoryImpl implements UserRepository {

    private final JPAQueryFactory queryFactory;
    private final UserEntityRepository userEntityRepository;

    @Override
    public boolean existsById(Long id) {
        QUserEntity userEntity = QUserEntity.userEntity;

        Integer result = queryFactory
            .selectOne()
            .from(userEntity)
            .where(userEntity.id.eq(id))
            .fetchFirst();

        return result != null;
    }

    @Override
    public boolean existsByEmail(String email) {
        QUserEntity userEntity = QUserEntity.userEntity;

        Integer result = queryFactory
            .selectOne()
            .from(userEntity)
            .where(userEntity.email.eq(email))
            .fetchFirst();

        return result != null;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userEntityRepository.findById(id).map(UserEntity::toUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userEntityRepository.findByEmail(email).map(UserEntity::toUser);
    }

    @Override
    public List<User> findAllById(List<Long> ids) {
        return userEntityRepository.findAllById(ids)
            .stream()
            .map(UserEntity::toUser)
            .toList();
    }

    @Override
    public void save(User user) {
        userEntityRepository.save(UserEntity.from(user));
    }

}
