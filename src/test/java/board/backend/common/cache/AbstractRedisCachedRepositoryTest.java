package board.backend.common.cache;

import board.backend.common.cache.infra.CachedRepository;
import board.backend.common.config.TestRedisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Import(DummyRedisCachedRepository.class)
class AbstractRedisCachedRepositoryTest extends TestRedisRepository {

    @Autowired
    CachedRepository<Dummy, Long> repository;

    @Test
    @DisplayName("Redis에 값을 저장하고 다시 조회할 수 있다")
    void saveAndFind_success() {
        // given
        Long key = 1L;
        Dummy value = new Dummy(key, "hello");

        // when
        repository.save(key, value, Duration.ofMinutes(5));
        Optional<Dummy> result = repository.findByKey(key);

        // then
        assertThat(result).contains(value);
    }

    @Test
    @DisplayName("값이 없으면 Optional.empty를 반환한다")
    void findByKey_notFound() {
        // when
        Optional<Dummy> result = repository.findByKey(999L);

        // then
        assertThat(result).isEmpty();
    }

}