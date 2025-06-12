package board.backend.user.application;

import board.backend.common.infra.CachedRepository;
import board.backend.user.domain.User;
import board.backend.user.domain.UserNotFound;
import board.backend.user.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@NamedInterface
@RequiredArgsConstructor
@Component
public class UserReader {

    private final static Duration CACHE_TTL = Duration.ofMinutes(5);

    private final CachedRepository<User, Long> cachedUserRepository;
    private final UserRepository userRepository;

    public void checkUserExistsOrThrow(Long userId) {
        if (!cachedUserRepository.existsByKey(userId) && !userRepository.customExistsById(userId)) {
            throw new UserNotFound();
        }
        cachedUserRepository.save(userId, null, CACHE_TTL);
    }

    public boolean isUserExists(Long userId) {
        if (cachedUserRepository.existsByKey(userId) || userRepository.customExistsById(userId)) {
            cachedUserRepository.save(userId, null, CACHE_TTL);
            return true;
        }

        return false;
    }

    public Optional<User> read(String email) {
        return userRepository.findByEmail(email);
    }

    User read(Long userId) {
        return cachedUserRepository.findByKey(userId)
            .orElseGet(() -> {
                User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
                cachedUserRepository.save(userId, user, CACHE_TTL);
                return user;
            });
    }

    public Map<Long, User> readAll(List<Long> userIds) {
        // 캐시 조회
        List<User> cached = cachedUserRepository.finalAllByKey(userIds);

        Map<Long, User> map = cached.stream()
            .collect(Collectors.toMap(User::getId, Function.identity()));

        // 캐시 미스만 조회
        List<Long> missed = userIds.stream()
            .filter(id -> !map.containsKey(id))
            .toList();
        if (!missed.isEmpty()) {
            List<User> uncached = userRepository.findAllById(missed);

            // 캐시에 저장
            uncached.forEach(user -> cachedUserRepository.save(user.getId(), user, CACHE_TTL));

            // 합쳐서 반환
            uncached.forEach(user -> map.put(user.getId(), user));
        }

        return map;
    }

}
