package board.backend.view.infra.jpa;

import board.backend.common.count.application.port.ArticleCountSnapshotRepository;
import board.backend.view.domain.ArticleViewCountSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
class ArticleViewCountSnapshotRepositoryImpl implements ArticleCountSnapshotRepository<ArticleViewCountSnapshot> {

    private final JdbcTemplate jdbcTemplate;
    private final ArticleViewCountSnapshotEntityRepository articleViewCountSnapshotEntityRepository;

    @Override
    public Optional<ArticleViewCountSnapshot> findByDateAndArticleId(LocalDate date, Long articleId) {
        return articleViewCountSnapshotEntityRepository.findBySnapshotDateAndArticleId(date, articleId)
            .map(ArticleViewCountSnapshotEntity::toArticleViewCountSnapshot);
    }

    @Override
    public void saveAll(List<ArticleViewCountSnapshot> snapshots) {
        jdbcTemplate.batchUpdate(
            "INSERT INTO article_view_count_snapshot (snapshot_date, article_id, view_count) VALUES (?, ?, ?)",
            snapshots,
            snapshots.size(),
            (ps, snapshot) -> {
                ps.setDate(1, Date.valueOf(snapshot.getDate()));
                ps.setLong(2, snapshot.getArticleId());
                ps.setLong(3, snapshot.getCount());
            }
        );
    }

}
