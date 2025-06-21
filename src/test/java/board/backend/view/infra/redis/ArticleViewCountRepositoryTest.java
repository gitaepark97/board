package board.backend.view.infra.redis;

import board.backend.common.infra.TestRedisRepository;
import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.domain.ArticleViewCount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Import(ArticleViewCountRepositoryImpl.class)
public class ArticleViewCountRepositoryTest extends TestRedisRepository {

    private final Long articleId1 = 1L;
    private final Long articleId2 = 2L;

    @Autowired
    private ArticleViewCountRepository articleViewCountRepository;

    @Test
    @DisplayName("조회수가 없으면 0으로 초기화되어 반환된다")
    void findById_whenNotExists_returnsZeroAndInitializes() {
        // when
        Long result = articleViewCountRepository.findById(articleId1);

        // then
        assertThat(result).isZero();
        assertThat(articleViewCountRepository.findById(articleId1)).isZero();
    }

    @Test
    @DisplayName("increase를 통해 조회수를 증가시킬 수 있다")
    void increase_success() {
        // when
        Long result = articleViewCountRepository.increase(articleId1);

        // then
        assertThat(result).isEqualTo(1L);
    }

    @Test
    @DisplayName("save를 통해 조회수를 저장할 수 있다")
    void save_success() {
        // given
        articleViewCountRepository.save(new ArticleViewCount(articleId1, 42L));

        // when
        Long result = articleViewCountRepository.findById(articleId1);

        // then
        assertThat(result).isEqualTo(42L);
    }

    @Test
    @DisplayName("deleteById를 통해 특정 ID의 조회수를 삭제할 수 있다")
    void deleteById_success() {
        // given
        articleViewCountRepository.increase(articleId1);

        // when
        articleViewCountRepository.deleteById(articleId1);

        // then
        Long result = articleViewCountRepository.findById(articleId1);
        assertThat(result).isZero();
    }

    @Test
    @DisplayName("findAllById로 여러 게시글의 조회수를 조회할 수 있다")
    void findAllById_success() {
        // given
        articleViewCountRepository.save(new ArticleViewCount(articleId1, 10L));
        articleViewCountRepository.save(new ArticleViewCount(articleId2, 20L));

        // when
        var result = articleViewCountRepository.findAllById(List.of(articleId1, articleId2, 999L));

        // then
        assertThat(result).containsEntry(articleId1, 10L);
        assertThat(result).containsEntry(articleId2, 20L);
        assertThat(result).containsEntry(999L, 0L);
    }

    @Test
    @DisplayName("findAll로 전체 조회수 정보를 조회할 수 있다")
    void findAll_success() {
        // given
        articleViewCountRepository.save(new ArticleViewCount(articleId1, 5L));
        articleViewCountRepository.save(new ArticleViewCount(articleId2, 7L));

        // when
        var result = articleViewCountRepository.findAll();

        // then
        assertThat(result)
            .extracting(ArticleViewCount::articleId, ArticleViewCount::viewCount)
            .containsExactlyInAnyOrder(
                tuple(articleId1, 5L),
                tuple(articleId2, 7L)
            );
    }

}
