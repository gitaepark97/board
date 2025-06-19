package board.backend.common.event;

public interface EventPublisher {

    void publishEvent(EventType type, EventPayload eventPayload);

}
