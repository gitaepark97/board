package board.backend.hotArticle.infra.redis;

import board.backend.common.infra.TestRedisRepository;
import board.backend.hotArticle.application.port.DailyArticleViewCountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Import(DailyArticleViewCountRepositoryImpl.class)
class DailyArticleViewCountRepositoryTest extends TestRedisRepository {

    private final Long articleId = 1L;
    private final LocalDateTime now = LocalDateTime.of(2025, 6, 19, 10, 0);
    private final Duration ttl = Duration.ofDays(1);

    @Autowired
    private DailyArticleViewCountRepository repository;

    @Test
    @DisplayName("저장되지 않은 경우 read는 0을 반환한다")
    void read_whenKeyNotExists_returnsZero() {
        // when
        long result = repository.read(articleId, now);

        // then
        assertThat(result).isZero();
    }

    @Test
    @DisplayName("view count를 저장하고 다시 조회하면 값이 일치한다")
    void saveAndRead_success() {
        // given
        long count = 42L;
        repository.save(articleId, count, now, ttl);

        // when
        long result = repository.read(articleId, now);

        // then
        assertThat(result).isEqualTo(count);
    }

    @Test
    @DisplayName("articleId로 관련된 모든 키를 삭제할 수 있다")
    void deleteById_removesAllKeys() {
        // given
        repository.save(articleId, 100L, now, ttl);

        // when
        repository.deleteById(articleId);

        // then
        long result = repository.read(articleId, now);
        assertThat(result).isZero();
    }

}