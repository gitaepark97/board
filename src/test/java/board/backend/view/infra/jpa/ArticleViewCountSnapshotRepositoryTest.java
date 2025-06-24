package board.backend.view.infra.jpa;

import board.backend.common.config.TestJpaRepository;
import board.backend.common.count.application.port.ArticleCountSnapshotRepository;
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
        ArticleViewCountSnapshot snapshot1 = ArticleViewCountSnapshot.builder()
            .date(date)
            .articleId(1L)
            .count(100L)
            .build();
        ArticleViewCountSnapshot snapshot2 = ArticleViewCountSnapshot.builder()
            .date(date)
            .articleId(2L)
            .count(200L)
            .build();
        List<ArticleViewCountSnapshot> snapshots = List.of(snapshot1, snapshot2);

        // when
        articleCountSnapshotRepository.saveAll(snapshots);

        // then
        var saved1 = articleCountSnapshotRepository.findByDateAndArticleId(date, snapshot1.getArticleId())
            .orElseThrow();
        assertThat(saved1.getCount()).isEqualTo(snapshot1.getCount());
        var saved2 = articleCountSnapshotRepository.findByDateAndArticleId(date, snapshot2.getArticleId())
            .orElseThrow();
        assertThat(saved2.getCount()).isEqualTo(snapshot2.getCount());

    }

}