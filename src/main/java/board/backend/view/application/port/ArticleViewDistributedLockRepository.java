package board.backend.view.application.port;

import java.time.Duration;

public interface ArticleViewDistributedLockRepository {

    Boolean lock(Long articleId, String ip, Duration ttl);

}
