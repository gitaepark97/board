package board.backend.common.event;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableRetry
@EnableAsync
class EventConfig {

}
