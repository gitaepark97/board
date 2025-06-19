package board.backend.hotArticle.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
class TimeCalculator {

    public Duration calculateDurationToNoon(LocalDateTime time) {
        LocalDateTime noon = time.plusDays(1).toLocalDate().atTime(12, 0);
        return Duration.between(time, noon);
    }

}
