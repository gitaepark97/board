package example.hugo.infra;

import example.hugo.application.TimeProvider;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
class SystemTime implements TimeProvider {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

}
