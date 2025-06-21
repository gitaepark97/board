package board.backend.like.infra.jpa;

import board.backend.common.application.port.ArticleCountSnapshotRepository;
import board.backend.like.domain.ArticleLikeCountSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
class ArticleLikeCountSnapshotRepositoryImpl implements ArticleCountSnapshotRepository<ArticleLikeCountSnapshot> {

    private final JdbcTemplate jdbcTemplate;
    private final ArticleLikeCountSnapshotEntityRepository articleLikeCountSnapshotEntityRepository;

    @Override
    public Optional<ArticleLikeCountSnapshot> findByDateAndArticleId(LocalDate date, Long articleId) {
        return articleLikeCountSnapshotEntityRepository.findBySnapshotDateAndArticleId(date, articleId)
            .map(ArticleLikeCountSnapshotEntity::toArticleViewCountSnapshot);
    }

    @Override
    public void saveAll(List<ArticleLikeCountSnapshot> snapshots) {
        jdbcTemplate.batchUpdate(
            "INSERT INTO article_like_count_snapshot (snapshot_date, article_id, like_count) VALUES (?, ?, ?)",
            snapshots,
            snapshots.size(),
            (ps, snapshot) -> {
                ps.setDate(1, Date.valueOf(snapshot.date()));
                ps.setLong(2, snapshot.articleId());
                ps.setLong(3, snapshot.likeCount());
            }
        );
    }

}
