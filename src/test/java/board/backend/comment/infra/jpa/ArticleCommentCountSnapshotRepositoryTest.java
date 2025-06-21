package board.backend.comment.infra.jpa;

import board.backend.comment.domain.ArticleCommentCountSnapshot;
import board.backend.common.application.port.ArticleCountSnapshotRepository;
import board.backend.common.infra.TestJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Import({ArticleCommentCountSnapshotRepositoryImpl.class})
class ArticleCommentCountSnapshotRepositoryTest extends TestJpaRepository {

    private final LocalDate date = LocalDate.of(2025, 6, 19);

    @Autowired
    private ArticleCountSnapshotRepository<ArticleCommentCountSnapshot> articleCountSnapshotRepository;

    @Test
    @DisplayName("스냅샷들을 배치로 저장할 수 있다")
    void saveAll_success() {
        // given
        ArticleCommentCountSnapshot snapshot1 = new ArticleCommentCountSnapshot(date, 1L, 100L);
        ArticleCommentCountSnapshot snapshot2 = new ArticleCommentCountSnapshot(date, 2L, 200L);
        List<ArticleCommentCountSnapshot> snapshots = List.of(snapshot1, snapshot2);

        // when
        articleCountSnapshotRepository.saveAll(snapshots);

        // then
        var saved1 = articleCountSnapshotRepository.findByDateAndArticleId(date, snapshot1.articleId())
            .orElseThrow();
        assertThat(saved1.commentCount()).isEqualTo(snapshot1.commentCount());
        var saved2 = articleCountSnapshotRepository.findByDateAndArticleId(date, snapshot2.articleId())
            .orElseThrow();
        assertThat(saved2.commentCount()).isEqualTo(snapshot2.commentCount());

    }

}