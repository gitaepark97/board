package board.backend.application;

import board.backend.domain.Session;
import board.backend.domain.SessionInvalid;
import board.backend.infra.SessionRepository;
import board.backend.support.TimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class SessionReader {

    private final TimeProvider timeProvider;
    private final SessionRepository sessionRepository;

    Session read(String sessionId) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(SessionInvalid::new);

        session.checkBlocked();
        session.checkExpired(timeProvider.now());

        return session;
    }

}
