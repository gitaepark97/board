package board.backend.user.application;

import board.backend.common.cache.infra.CachedRepository;
import board.backend.common.support.TimeProvider;
import board.backend.user.application.port.UserRepository;
import board.backend.user.domain.User;
import board.backend.user.domain.UserNotFound;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class UserUpdater {

    private final TimeProvider timeProvider;
    private final CachedRepository<User, Long> cachedUserRepository;
    private final UserRepository userRepository;

    @Transactional
    User update(Long userId, String nickname) {
        // 사용자 조회
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        // 캐시 삭제
        cachedUserRepository.delete(userId);

        // 사용자 수정
        User updatedUser = user.update(nickname, timeProvider.now());
        // 사용자 저장
        userRepository.save(updatedUser);

        return updatedUser;
    }

}
