package board.backend.user.application;

import board.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserReader userReader;
    private final UserUpdater userUpdater;

    public User read(Long userId) {
        return userReader.read(userId);
    }

    public User update(Long userId, String nickname) {
        return userUpdater.update(userId, nickname);
    }

}
