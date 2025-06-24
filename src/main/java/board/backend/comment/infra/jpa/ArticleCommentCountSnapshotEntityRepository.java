package board.backend.comment.infra.jpa;

import board.backend.common.count.infra.ArticleCountSnapshotId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

interface ArticleCommentCountSnapshotEntityRepository extends JpaRepository<ArticleCommentCountSnapshotEntity, ArticleCountSnapshotId> {

    Optional<ArticleCommentCountSnapshotEntity> findBySnapshotDateAndArticleId(LocalDate snapshotDate, Long articleId);

}
