package board.backend.view.infra.redis;

import board.backend.common.config.TestRedisRepository;
import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.domain.ArticleViewCount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        Optional<ArticleViewCount> result = articleViewCountRepository.findById(articleId1);

        // then
        assertThat(result).isEmpty();
    }

}
