package board.backend.hotArticle.application.port;

import java.time.Duration;
import java.time.LocalDateTime;

public interface DailyArticleCountRepository {

    Long read(Long articleId, LocalDateTime time);

    void increaseOrSave(Long articleId, LocalDateTime now, Duration ttl);

    void decrease(Long articleId, LocalDateTime now);

    void deleteById(Long articleId);

}
