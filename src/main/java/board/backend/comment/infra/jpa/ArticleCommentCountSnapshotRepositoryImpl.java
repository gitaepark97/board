package board.backend.comment.infra.jpa;

import board.backend.comment.domain.ArticleCommentCountSnapshot;
import board.backend.common.count.application.port.ArticleCountSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
class ArticleCommentCountSnapshotRepositoryImpl implements ArticleCountSnapshotRepository<ArticleCommentCountSnapshot> {

    private final JdbcTemplate jdbcTemplate;
    private final ArticleCommentCountSnapshotEntityRepository articleCommentCountSnapshotEntityRepository;

    @Override
    public Optional<ArticleCommentCountSnapshot> findByDateAndArticleId(LocalDate date, Long articleId) {
        return articleCommentCountSnapshotEntityRepository.findBySnapshotDateAndArticleId(date, articleId)
            .map(ArticleCommentCountSnapshotEntity::toArticleViewCountSnapshot);
    }

    @Override
    public void saveAll(List<ArticleCommentCountSnapshot> snapshots) {
        jdbcTemplate.batchUpdate(
            "INSERT INTO article_comment_count_snapshot (snapshot_date, article_id, comment_count) VALUES (?, ?, ?)",
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
