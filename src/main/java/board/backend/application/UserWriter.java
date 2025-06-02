package board.backend.application;

import board.backend.domain.User;
import board.backend.domain.UserEmailDuplicated;
import board.backend.domain.UserNotFound;
import board.backend.infra.UserRepository;
import board.backend.support.IdProvider;
import board.backend.support.TimeProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class UserWriter {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final UserRepository userRepository;

    @Transactional
    User create(String email, String nickname) {
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
