package board.backend.common.event;

import board.backend.common.support.IdProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class EventPublisherImpl implements EventPublisher {

    private final IdProvider idProvider;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    @Override
    public void publishEvent(EventType type, EventPayload eventPayload) {
        Event<EventPayload> event = Event.create(idProvider.nextId(), type, eventPayload);
        applicationEventPublisher.publishEvent(event);
    }

}
