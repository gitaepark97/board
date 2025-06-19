package board.backend.hotArticle.infra.redis;

import board.backend.common.infra.TestRedisRepository;
import board.backend.hotArticle.application.port.HotArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Import(HotArticleRepositoryImpl.class)
class HotArticleRepositoryTest extends TestRedisRepository {

    private final LocalDateTime now = LocalDateTime.of(2025, 6, 19, 10, 0);
    private final Duration ttl = Duration.ofDays(1);

    @Autowired
    private HotArticleRepository repository;
    
    @Test
    @DisplayName("저장된 게시글을 날짜 기준으로 점수순 정렬하여 조회한다")
    void readAll_success_returnsSortedArticles() {
        // given
        repository.save(2L, now, 30L, 10L, ttl);
        repository.save(1L, now, 50L, 10L, ttl);
        repository.save(3L, now, 10L, 10L, ttl);

        // when
        var result = repository.readAll("20250619");

        // then
        assertThat(result).containsExactly(1L, 2L, 3L);
    }

    @Test
    @DisplayName("limit을 초과하면 점수가 낮은 게시글은 제거된다")
    void save_success_appliesLimitProperly() {
        // given
        for (long i = 1; i <= 15; i++) {
            repository.save(i, now, i * 10, 10L, ttl);
        }

        // when
        var result = repository.readAll("20250619");

        // then
        assertThat(result).hasSize(10);
        assertThat(result.getFirst()).isEqualTo(15L);
        assertThat(result.getLast()).isEqualTo(6L);
    }

    @Test
    @DisplayName("delete를 호출하면 모든 날짜 키에서 해당 게시글이 제거된다")
    void delete_success_removesFromAllKeys() {
        // given
        Long articleId = 1L;
        repository.save(articleId, now, 100L, 10L, ttl);

        // when
        repository.delete(articleId);

        // then
        var result = repository.readAll("20250619");
        assertThat(result).doesNotContain(articleId);
    }

}