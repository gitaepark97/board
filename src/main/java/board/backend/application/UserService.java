package board.backend.application;

import board.backend.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserReader userReader;
    private final UserWriter userWriter;

    public User read(Long userId) {
        return userReader.read(userId);
    }

    public User update(Long userId, String nickname) {
        return userWriter.update(userId, nickname);
    }

}
