package board.backend.view.application.port;

import java.time.LocalDateTime;

public interface ArticleViewBackupTimeRepository {

    void update(Long articleId, LocalDateTime backupTime);

    LocalDateTime findById(Long articleId);

}
