package board.backend.user.application;

import board.backend.common.infra.CachedRepository;
import board.backend.common.support.IdProvider;
import board.backend.common.support.TimeProvider;
import board.backend.user.application.port.UserRepository;
import board.backend.user.domain.User;
import board.backend.user.domain.UserEmailDuplicated;
import board.backend.user.domain.UserNotFound;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

@NamedInterface
@RequiredArgsConstructor
@Component
public class UserWriter {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final CachedRepository<User, Long> cachedUserRepository;
    private final UserRepository userRepository;

    @Transactional
    public User create(String email, String nickname) {
        // 이메일 중복 확인
        checkEmailUniqueOrThrow(email);

        // 회원 생성
        User newUser = User.create(idProvider.nextId(), email, nickname, timeProvider.now());
        // 회원 저장
        userRepository.save(newUser);

        return newUser;
    }

    @Transactional
    User update(Long userId, String nickname) {
        // 회원 조회
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        // 캐시 삭제
        cachedUserRepository.delete(userId);

        // 회원 수정
        User updatedUser = user.update(nickname, timeProvider.now());
        // 회원 저장
        userRepository.save(updatedUser);

        return updatedUser;
    }

    private void checkEmailUniqueOrThrow(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicated();
        }
    }

}
