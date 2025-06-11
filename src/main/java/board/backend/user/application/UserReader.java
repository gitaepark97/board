package board.backend.user.application;

import board.backend.common.infra.CacheRepository;
import board.backend.user.domain.User;
import board.backend.user.domain.UserNotFound;
import board.backend.user.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserReader {

    private final static Duration CACHE_TTL = Duration.ofMinutes(5);

    private final CacheRepository<User, Long> userCacheRepository;
    private final UserRepository userRepository;

    public void checkUserExists(Long userId) {
        if (userCacheRepository.get(userId).isEmpty() && !userRepository.customExistsById(userId)) {
            throw new UserNotFound();
        }
    }

    public boolean isUserExists(Long userId) {
        return userCacheRepository.get(userId).isPresent() || userRepository.customExistsById(userId);
    }

    public Optional<User> read(String email) {
        return userRepository.findByEmail(email);
    }

    User read(Long userId) {
        return userCacheRepository.get(userId)
            .orElseGet(() -> {
                User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
                userCacheRepository.set(userId, user, CACHE_TTL);
                return user;
            });
    }

    public Map<Long, User> readAll(List<Long> userIds) {
        // 캐시 조회
        List<User> cached = userCacheRepository.getAll(userIds);

        Map<Long, User> map = cached.stream()
            .collect(Collectors.toMap(User::getId, Function.identity()));

        // 캐시 미스만 조회
        List<Long> missed = userIds.stream()
            .filter(id -> !map.containsKey(id))
            .toList();
        if (!missed.isEmpty()) {
            List<User> uncached = userRepository.findAllById(missed);

            // 캐시에 저장
            uncached.forEach(user -> userCacheRepository.set(user.getId(), user, CACHE_TTL));

            // 합쳐서 반환
            uncached.forEach(user -> map.put(user.getId(), user));
        }

        return map;
    }

}
