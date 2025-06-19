package board.backend.auth.application;

import board.backend.auth.application.port.SessionRepository;
import board.backend.auth.domain.Session;
import board.backend.common.support.TimeProvider;
import board.backend.common.support.UUIDProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class SessionCreator {

    private final UUIDProvider uuidProvider;
    private final TimeProvider timeProvider;
    private final SessionRepository sessionRepository;

    Session create(Long userId) {
        // 세션 생성
        Session newSession = Session.create(
            uuidProvider.random(),
            userId,
            timeProvider.now()
        );
        // 세션 저장
        sessionRepository.save(newSession);

        return newSession;
    }

}
