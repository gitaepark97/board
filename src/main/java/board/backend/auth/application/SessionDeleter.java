package board.backend.auth.application;

import board.backend.auth.application.port.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class SessionDeleter {

    private final SessionRepository sessionRepository;

    @Transactional
    void delete(Long userId) {
        sessionRepository.deleteByUserId(userId);
    }

}
