package board.backend.user.application;

import board.backend.common.cache.infra.CachedRepository;
import board.backend.user.application.port.UserRepository;
import board.backend.user.domain.User;
import board.backend.user.domain.UserNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

@NamedInterface
@RequiredArgsConstructor
@Component
public class UserValidator {

    private final CachedRepository<User, Long> cachedUserRepository;
    private final UserRepository userRepository;

    public void checkUserExistsOrThrow(Long userId) {
        if (!cachedUserRepository.existsByKey(userId) && !userRepository.existsById(userId)) {
            throw new UserNotFound();
        }

        cachedUserRepository.save(userId, null, UserConstants.USER_CACHE_TTL);
    }

    public boolean isUserExists(Long userId) {
        if (!cachedUserRepository.existsByKey(userId) && !userRepository.existsById(userId)) {
            return false;
        }

        cachedUserRepository.save(userId, null, UserConstants.USER_CACHE_TTL);
        return true;
    }

}
