package board.backend.application;

import board.backend.domain.Session;
import board.backend.infra.SessionRepository;
import board.backend.support.TimeProvider;
import board.backend.support.UUIDProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class SessionWriter {

    private final UUIDProvider uuidProvider;
    private final TimeProvider timeProvider;
    private final SessionRepository sessionRepository;

    @Transactional
    Session create(Long userId) {
        // 기존 세션 삭제
        delete(userId);

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

    @Transactional
    void delete(Long userId) {
        sessionRepository.deleteByUserId(userId);
    }

}
