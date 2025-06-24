package board.backend.view.infra.redis;

import board.backend.common.config.TestRedisRepository;
import board.backend.view.application.port.ArticleViewDistributedLockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@Import(ArticleViewDistributedLockRepositoryImpl.class)
class ArticleViewDistributedLockRepositoryTest extends TestRedisRepository {

    private final Long articleId = 1L;
    private final String ip = "127.0.0.1";
    private final Duration ttl = Duration.ofSeconds(3);

    @Autowired
    @Qualifier("lockStringRedisTemplate")
    protected StringRedisTemplate redisTemplate;
    @Autowired
    private ArticleViewDistributedLockRepository repository;

    @AfterEach
    void clearRedis() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @Test
    @DisplayName("잠금이 설정되지 않았을 경우 lock을 획득할 수 있다")
    void lock_success_whenNotLocked() {
        // when
        boolean locked = repository.lock(articleId, ip, ttl);

        // then
        assertThat(locked).isTrue();
    }

    @Test
    @DisplayName("이미 잠금이 설정된 경우 lock을 획득할 수 없다")
    void lock_fails_whenAlreadyLocked() {
        // given
        repository.lock(articleId, ip, ttl);

        // when
        boolean lockedAgain = repository.lock(articleId, ip, ttl);

        // then
        assertThat(lockedAgain).isFalse();
    }

    @Test
    @DisplayName("TTL이 만료되면 다시 lock을 획득할 수 있다")
    void lock_success_afterTtlExpires() throws InterruptedException {
        // given
        repository.lock(articleId, ip, Duration.ofMillis(500));

        // when
        Thread.sleep(600); // wait for TTL to expire
        boolean lockedAgain = repository.lock(articleId, ip, ttl);

        // then
        assertThat(lockedAgain).isTrue();
    }

}