package board.backend.user.application;

import board.backend.common.support.IdProvider;
import board.backend.common.support.TimeProvider;
import board.backend.user.application.port.UserRepository;
import board.backend.user.domain.User;
import board.backend.user.domain.UserEmailDuplicated;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

@NamedInterface
@RequiredArgsConstructor
@Component
public class UserCreator {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final UserRepository userRepository;

    @Transactional
    public User create(String email, String nickname) {
        // 이메일 중복 확인
        checkEmailUniqueOrThrow(email);

        // 사용자 생성
        User newUser = User.create(idProvider.nextId(), email, nickname, timeProvider.now());
        // 사용자 저장
        userRepository.save(newUser);

        return newUser;
    }

    private void checkEmailUniqueOrThrow(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicated();
        }
    }

}
