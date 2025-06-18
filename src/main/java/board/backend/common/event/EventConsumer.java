package board.backend.common.event;

import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
class EventConsumer {

    private final List<EventHandler> eventHandlers;

    @ApplicationModuleListener
    void handleEvent(Event<EventPayload> event) {
        eventHandlers.stream()
            .filter(eventHandler -> eventHandler.supports(event))
            .forEach(handler -> handler.handle(event));
    }

}
