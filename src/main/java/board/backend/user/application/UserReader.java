package board.backend.user.application;

import board.backend.user.domain.User;
import board.backend.user.domain.UserNotFound;
import board.backend.user.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserReader {

    private final UserRepository userRepository;

    public void checkUserExists(Long userId) {
        if (!userRepository.customExistsById(userId)) {
            throw new UserNotFound();
        }
    }

    public boolean isUserExists(Long userId) {
        return userRepository.customExistsById(userId);
    }

    public Optional<User> read(String email) {
        return userRepository.findByEmail(email);
    }

    User read(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFound::new);
    }

    public Map<Long, User> readAll(List<Long> userIds) {
        return userRepository.findAllById(userIds)
            .stream()
            .collect(Collectors.toMap(User::getId, Function.identity()));
    }

}
