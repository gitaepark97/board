package board.backend.hotArticle.application;

import board.backend.common.support.TimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
class TimeCalculator {

    private final TimeProvider timeProvider;

    public Duration calculateDurationToNoon() {
        LocalDateTime now = timeProvider.now();
        LocalDateTime noon = now.plusDays(1).toLocalDate().atTime(12, 0);
        return Duration.between(now, noon);
    }

}
