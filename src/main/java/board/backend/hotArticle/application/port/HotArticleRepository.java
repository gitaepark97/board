package board.backend.hotArticle.application.port;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public interface HotArticleRepository {

    List<Long> readAll(String dateStr);

    void save(Long articleId, LocalDateTime time, Long score, Long limit, Duration ttl);

    void delete(Long articleId);

}
