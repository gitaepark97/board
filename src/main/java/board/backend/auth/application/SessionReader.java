package board.backend.auth.application;

import board.backend.auth.application.port.SessionRepository;
import board.backend.auth.domain.Session;
import board.backend.auth.domain.SessionInvalid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class SessionReader {

    private final SessionRepository sessionRepository;

    Session read(String sessionId) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(SessionInvalid::new);

        session.checkBlocked();

        return session;
    }

}
