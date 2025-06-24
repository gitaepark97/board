package board.backend.common.support;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface TimeProvider {

    LocalDateTime now();

    LocalDate today();

    default LocalDate yesterday() {
        return now().toLocalDate().minusDays(1);
    }

    default boolean isToday(LocalDateTime time) {
        return time.toLocalDate().isEqual(today());
    }

}
