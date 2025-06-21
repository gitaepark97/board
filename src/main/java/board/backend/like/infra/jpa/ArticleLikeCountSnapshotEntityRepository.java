package board.backend.like.infra.jpa;

import board.backend.common.infra.ArticleCountSnapshotId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

interface ArticleLikeCountSnapshotEntityRepository extends JpaRepository<ArticleLikeCountSnapshotEntity, ArticleCountSnapshotId> {

    Optional<ArticleLikeCountSnapshotEntity> findBySnapshotDateAndArticleId(LocalDate snapshotDate, Long articleId);

}
