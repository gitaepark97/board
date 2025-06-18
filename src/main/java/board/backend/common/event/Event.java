package board.backend.common.event;

import board.backend.common.support.DataSerializer;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Event<T extends EventPayload> {

    private Long id;
    private EventType type;
    private T payload;

    public static Event<EventPayload> create(Long id, EventType type, EventPayload payload) {
        return Event.builder()
            .id(id)
            .type(type)
            .payload(payload)
            .build();
    }

    public static Event<EventPayload> fromJson(String json) {
        EventRaw eventRaw = DataSerializer.deserialize(json, EventRaw.class);
        if (eventRaw == null) {
            return null;
        }
        EventType eventType = EventType.from(eventRaw.type);
        assert eventType != null;

        return Event.builder()
            .id(eventRaw.id)
            .type(eventType)
            .payload(DataSerializer.deserialize(eventRaw.payload(), eventType.getPayloadClass()))
            .build();
    }

    public String toJson() {
        return DataSerializer.serialize(this);
    }

    private record EventRaw(
        Long id,
        String type,
        Object payload
    ) {

    }

}
