package board.backend.common.application.port;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ArticleCountSnapshotRepository<T> {

    Optional<T> findByDateAndArticleId(LocalDate date, Long articleId);

    void saveAll(List<T> snapshots);

}
