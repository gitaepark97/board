package board.backend.user.application.port;

import board.backend.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    boolean existsById(Long id);

    boolean existsByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAllById(List<Long> ids);

    void save(User user);

}
