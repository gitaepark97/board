package board.backend.application;

import board.backend.domain.User;
import board.backend.domain.UserNotFound;
import board.backend.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
class UserReader {

    private final UserRepository userRepository;

    Optional<User> read(String email) {
        return userRepository.findByEmail(email);
    }

    User read(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFound::new);
    }

    boolean isUserExists(Long userId) {
        return userRepository.customExistsById(userId);
    }

    Map<Long, User> readAll(List<Long> userIds) {
        return userRepository.findAllById(userIds)
            .stream()
            .collect(Collectors.toMap(User::getId, Function.identity()));
    }

}
