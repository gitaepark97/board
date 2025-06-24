package board.backend.user.application;

import board.backend.common.cache.infra.CachedRepository;
import board.backend.user.application.port.UserRepository;
import board.backend.user.domain.User;
import board.backend.user.domain.UserNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static board.backend.user.application.UserConstants.USER_CACHE_TTL;
import static java.util.function.Predicate.not;

@NamedInterface
@RequiredArgsConstructor
@Component
public class UserReader {

    private final CachedRepository<User, Long> cachedUserRepository;
    private final UserRepository userRepository;

    public Optional<User> read(String email) {
        return userRepository.findByEmail(email);
    }

    public User read(Long userId) {
        return cachedUserRepository.findByKey(userId)
            .orElseGet(() -> {
                User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
                cachedUserRepository.save(userId, user, USER_CACHE_TTL);
                return user;
            });
    }

    public Map<Long, User> readAll(List<Long> userIds) {
        // 캐시 조회
        List<User> cached = cachedUserRepository.findAllByKey(userIds);

        Map<Long, User> map = cached.stream()
            .collect(Collectors.toMap(User::id, Function.identity()));

        // 캐시 미스만 조회
        List<Long> missed = userIds.stream()
            .filter(not(map::containsKey))
            .toList();
        if (!missed.isEmpty()) {
            List<User> uncached = userRepository.findAllById(missed);

            // 캐시에 저장
            uncached.forEach(user -> cachedUserRepository.save(user.id(), user, USER_CACHE_TTL));

            // 합쳐서 반환
            uncached.forEach(user -> map.put(user.id(), user));
        }

        return map;
    }

}
