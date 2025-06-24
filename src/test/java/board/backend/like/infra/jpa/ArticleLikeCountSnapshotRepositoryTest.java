package board.backend.like.infra.jpa;

import board.backend.common.config.TestJpaRepository;
import board.backend.common.count.application.port.ArticleCountSnapshotRepository;
import board.backend.like.domain.ArticleLikeCountSnapshot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Import({ArticleLikeCountSnapshotRepositoryImpl.class})
class ArticleLikeCountSnapshotRepositoryTest extends TestJpaRepository {

    private final LocalDate date = LocalDate.of(2025, 6, 19);

    @Autowired
    private ArticleCountSnapshotRepository<ArticleLikeCountSnapshot> articleLikeCountSnapshotRepository;

    @Test
    @DisplayName("스냅샷들을 배치로 저장할 수 있다")
    void saveAll_success() {
        // given
        ArticleLikeCountSnapshot snapshot1 = ArticleLikeCountSnapshot.builder()
            .date(date)
            .articleId(1L)
            .count(100L)
            .build();
        ArticleLikeCountSnapshot snapshot2 = ArticleLikeCountSnapshot.builder()
            .date(date)
            .articleId(2L)
            .count(200L)
            .build();
        List<ArticleLikeCountSnapshot> snapshots = List.of(snapshot1, snapshot2);

        // when
        articleLikeCountSnapshotRepository.saveAll(snapshots);

        // then
        var saved1 = articleLikeCountSnapshotRepository.findByDateAndArticleId(date, snapshot1.getArticleId())
            .orElseThrow();
        assertThat(saved1.getCount()).isEqualTo(snapshot1.getCount());
        var saved2 = articleLikeCountSnapshotRepository.findByDateAndArticleId(date, snapshot2.getArticleId())
            .orElseThrow();
        assertThat(saved2.getCount()).isEqualTo(snapshot2.getCount());

    }

}