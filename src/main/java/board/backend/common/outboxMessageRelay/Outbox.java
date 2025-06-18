package board.backend.common.outboxMessageRelay;

import board.backend.common.event.EventType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "outbox")
public class Outbox {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private String payload;

    private LocalDateTime createdAt;

    static Outbox create(Long id, EventType eventType, String payload, LocalDateTime now) {
        return Outbox.builder()
            .id(id)
            .eventType(eventType)
            .payload(payload)
            .createdAt(now)
            .build();
    }

}
