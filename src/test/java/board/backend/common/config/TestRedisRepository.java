package board.backend.common.config;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@Execution(ExecutionMode.SAME_THREAD)
@ActiveProfiles("test")
@Import(RedisConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataRedisTest
public abstract class TestRedisRepository {

    private static final GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7.0"))
        .withExposedPorts(6379)
        .withReuse(true);

    @Autowired
    protected StringRedisTemplate redisTemplate;

    @DynamicPropertySource
    static void overrideRedisProperties(DynamicPropertyRegistry registry) {
        redis.start();

        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @AfterEach
    void clearRedis() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @AfterAll
    void afterAll() {
        redis.stop();
    }

}
