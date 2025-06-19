package board.backend.common.event.fake;

import board.backend.common.event.EventPayload;
import board.backend.common.event.EventPublisher;
import board.backend.common.event.EventType;

import java.util.ArrayList;
import java.util.List;

public class FakeEventPublisher implements EventPublisher {

    private final List<PublishedEvent> events = new ArrayList<>();

    @Override
    public void publishEvent(EventType type, EventPayload payload) {
        events.add(new PublishedEvent(type, payload));
    }

    public List<PublishedEvent> getPublishedEvents() {
        return events;
    }

    public record PublishedEvent(EventType type, EventPayload payload) {

    }

}
