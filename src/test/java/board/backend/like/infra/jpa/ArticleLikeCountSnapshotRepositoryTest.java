package board.backend.like.infra.jpa;

import board.backend.common.application.port.ArticleCountSnapshotRepository;
import board.backend.common.infra.TestJpaRepository;
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
        ArticleLikeCountSnapshot snapshot1 = new ArticleLikeCountSnapshot(date, 1L, 100L);
        ArticleLikeCountSnapshot snapshot2 = new ArticleLikeCountSnapshot(date, 2L, 200L);
        List<ArticleLikeCountSnapshot> snapshots = List.of(snapshot1, snapshot2);

        // when
        articleLikeCountSnapshotRepository.saveAll(snapshots);

        // then
        var saved1 = articleLikeCountSnapshotRepository.findByDateAndArticleId(date, snapshot1.articleId())
            .orElseThrow();
        assertThat(saved1.likeCount()).isEqualTo(snapshot1.likeCount());
        var saved2 = articleLikeCountSnapshotRepository.findByDateAndArticleId(date, snapshot2.articleId())
            .orElseThrow();
        assertThat(saved2.likeCount()).isEqualTo(snapshot2.likeCount());

    }

}