package board.backend.hotArticle.infra.redis;

import board.backend.common.config.TestRedisRepository;
import board.backend.hotArticle.application.port.DailyArticleCountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Import(DummyDailyArticleCountRepository.class)
class DailyArticleCountRepositoryImplTest extends TestRedisRepository {

    private final Long articleId = 1L;
    private final LocalDateTime now = LocalDateTime.of(2025, 6, 19, 10, 0);
    private final Duration ttl = Duration.ofDays(1);

    @Autowired
    private DailyArticleCountRepository repository;

    @Test
    @DisplayName("기존 값이 존재하지 않으면 0을 반환한다")
    void read_whenExists() {
        // given
        repository.save(articleId, 1L, now, ttl);

        // when
        Long result = repository.read(articleId, now);

        // then
        assertThat(result).isEqualTo(1L);
    }

    @Test
    @DisplayName("기존 값이 존재하지 않으면 0을 반환한다")
    void read_whenNotExists() {
        // when
        Long result = repository.read(articleId, now);

        // then
        assertThat(result).isZero();
    }

    @Test
    @DisplayName("값이 저장되면 정상적으로 읽을 수 있다")
    void save_success_storesValueCorrectly() {
        // when
        repository.save(articleId, 1L, now, ttl);

        // then
        Long result = repository.read(articleId, now);
        assertThat(result).isEqualTo(1L);
    }

    @Test
    @DisplayName("ID 기준으로 관련 key들이 삭제된다")
    void deleteByArticleId_removesKeys() {
        // given
        repository.save(articleId, 1L, now, ttl);
        repository.save(articleId, 2L, now.minusHours(1), ttl);

        // when
        repository.deleteByArticleId(articleId);

        // then
        assertThat(repository.read(articleId, now)).isZero();
        assertThat(repository.read(articleId, now.minusHours(1))).isZero();
    }

}