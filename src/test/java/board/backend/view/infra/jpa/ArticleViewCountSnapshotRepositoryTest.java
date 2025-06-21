package board.backend.view.infra.jpa;

import board.backend.common.application.port.ArticleCountSnapshotRepository;
import board.backend.common.infra.TestJpaRepository;
import board.backend.view.domain.ArticleViewCountSnapshot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Import({ArticleViewCountSnapshotRepositoryImpl.class})
class ArticleViewCountSnapshotRepositoryTest extends TestJpaRepository {

    private final LocalDate date = LocalDate.of(2025, 6, 19);

    @Autowired
    private ArticleCountSnapshotRepository<ArticleViewCountSnapshot> articleCountSnapshotRepository;

    @Test
    @DisplayName("스냅샷들을 배치로 저장할 수 있다")
    void saveAll_success() {
        // given
        ArticleViewCountSnapshot snapshot1 = new ArticleViewCountSnapshot(date, 1L, 100L);
        ArticleViewCountSnapshot snapshot2 = new ArticleViewCountSnapshot(date, 2L, 200L);
        List<ArticleViewCountSnapshot> snapshots = List.of(snapshot1, snapshot2);

        // when
        articleCountSnapshotRepository.saveAll(snapshots);

        // then
        var saved1 = articleCountSnapshotRepository.findByDateAndArticleId(date, snapshot1.articleId())
            .orElseThrow();
        assertThat(saved1.viewCount()).isEqualTo(snapshot1.viewCount());
        var saved2 = articleCountSnapshotRepository.findByDateAndArticleId(date, snapshot2.articleId())
            .orElseThrow();
        assertThat(saved2.viewCount()).isEqualTo(snapshot2.viewCount());

    }

}