package board.backend.common.support;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
class SystemTime implements TimeProvider {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDate today() {
        return LocalDate.now();
    }

}
