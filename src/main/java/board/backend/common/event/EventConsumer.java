package board.backend.common.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
class EventConsumer {

    private final List<EventHandler> eventHandlers;
    private final ApplicationEventPublisher eventPublisher;

    @Retryable(backoff = @Backoff(delay = 2000))
    @ApplicationModuleListener
    void handleEvent(Event<EventPayload> event) {
        eventHandlers.stream()
            .filter(eventHandler -> eventHandler.supports(event))
            .forEach(handler -> handler.handle(event));
    }

    @Recover
    void recover(Exception e, Event<EventPayload> event) {
        log.error("재시도 실패, 보상 처리 진행", e);
        eventPublisher.publishEvent(event);
    }

}
