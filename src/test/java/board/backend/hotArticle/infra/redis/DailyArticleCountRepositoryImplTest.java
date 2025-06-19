package board.backend.hotArticle.infra.redis;

import board.backend.common.infra.TestRedisRepository;
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
    @DisplayName("값이 존재하지 않으면 0을 반환한다")
    void read_whenNotExists() {
        // when
        Long result = repository.read(articleId, now);

        // then
        assertThat(result).isZero();
    }

    @Test
    @DisplayName("값이 없으면 1로 저장된다")
    void increaseOrSave_setsValue() {
        // when
        repository.increaseOrSave(articleId, now, ttl);

        // then
        assertThat(repository.read(articleId, now)).isEqualTo(1L);
    }

    @Test
    @DisplayName("기존 값이 존재하면 감소시킨다")
    void decrease_reducesValue() {
        // given
        repository.increaseOrSave(articleId, now, ttl);

        // when
        repository.decrease(articleId, now);

        // then
        assertThat(repository.read(articleId, now)).isEqualTo(0L);
    }

    @Test
    @DisplayName("ID 기준으로 관련 key들이 삭제된다")
    void deleteById_removesKeys() {
        // given
        repository.increaseOrSave(articleId, now, ttl);

        // when
        repository.deleteById(articleId);

        // then
        assertThat(repository.read(articleId, now)).isZero();
    }

}