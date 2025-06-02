package board.backend.application;

import board.backend.domain.User;
import board.backend.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
class UserReader {

    private final UserRepository userRepository;

    Optional<User> read(String email) {
        return userRepository.findByEmail(email);
    }

    boolean isUserExists(Long userId) {
        return userRepository.customExistsById(userId);
    }


}
