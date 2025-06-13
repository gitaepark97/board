package board.backend.user.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

}
