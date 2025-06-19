package board.backend.hotArticle.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TimeCalculatorTest {

    private TimeCalculator timeCalculator;

    @BeforeEach
    void setUp() {
        timeCalculator = new TimeCalculator();
    }

    @Test
    @DisplayName("내일 정오까지 남은 시간을 계산할 수 있다")
    void calculateDurationToNoon_success_returnsDurationUntilNextNoon() {
        // given
        LocalDateTime time = LocalDateTime.of(2024, 1, 1, 10, 0);

        // when
        Duration duration = timeCalculator.calculateDurationToNoon(time);

        // then
        assertThat(duration).isEqualTo(Duration.ofHours(26));
    }

}