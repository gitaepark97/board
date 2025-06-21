package board.backend.hotArticle.application.port;

import java.time.Duration;
import java.time.LocalDateTime;

public interface DailyArticleCountRepository {

    Long read(Long articleId, LocalDateTime time);

    void save(Long articleId, Long count, LocalDateTime time, Duration ttl);

    void deleteByArticleId(Long articleId);

}
