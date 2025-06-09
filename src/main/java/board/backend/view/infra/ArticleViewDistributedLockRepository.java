package board.backend.view.infra;

import java.time.Duration;

public interface ArticleViewDistributedLockRepository {

    Boolean lock(Long articleId, String ip, Duration ttl);

}
