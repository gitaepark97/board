package board.backend.view.infra.jpa;

import board.backend.common.infra.ArticleCountSnapshotId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

interface ArticleViewCountSnapshotEntityRepository extends JpaRepository<ArticleViewCountSnapshotEntity, ArticleCountSnapshotId> {

    Optional<ArticleViewCountSnapshotEntity> findBySnapshotDateAndArticleId(LocalDate snapshotDate, Long articleId);

}
