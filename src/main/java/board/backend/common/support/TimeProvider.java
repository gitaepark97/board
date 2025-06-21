package board.backend.common.support;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface TimeProvider {

    LocalDateTime now();

    default LocalDate yesterday() {
        return now().toLocalDate().minusDays(1);
    }

}
