package board.backend.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
class RedisConfig {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    @Primary
    LettuceConnectionFactory defaultFactory() {
        return createFactory(host, port, password, 0);
    }

    @Bean
    LettuceConnectionFactory lockFactory() {
        return createFactory(host, port, password, 1);
    }

    @Bean
    LettuceConnectionFactory cacheFactory() {
        return createFactory(host, port, password, 2);
    }

    @Bean
    @Primary
    RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory defaultFactory) {
        return getStringObjectRedisTemplate(defaultFactory);
    }

    @Bean
    @Primary
    StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory defaultFactory) {
        return getStringRedisTemplate(defaultFactory);
    }

    @Bean(name = "cacheRedisTemplate")
    RedisTemplate<String, Object> cacheRedisTemplate(@Qualifier("cacheFactory") LettuceConnectionFactory factory) {
        return getStringObjectRedisTemplate(factory);
    }

    @Bean(name = "lockStringRedisTemplate")
    StringRedisTemplate lockStringRedisTemplate(@Qualifier("lockFactory") LettuceConnectionFactory factory) {
        return getStringRedisTemplate(factory);
    }

    private StringRedisTemplate getStringRedisTemplate(LettuceConnectionFactory factory) {
        factory.afterPropertiesSet();

        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();

        return template;
    }

    private RedisTemplate<String, Object> getStringObjectRedisTemplate(LettuceConnectionFactory factory) {
        factory.afterPropertiesSet();

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, Object.class));
        template.afterPropertiesSet();

        return template;
    }

    private LettuceConnectionFactory createFactory(
        String host,
        int port,
        String password,
        int dbIndex
    ) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        config.setPassword(RedisPassword.of(password));
        config.setDatabase(dbIndex);

        return new LettuceConnectionFactory(config);
    }

}
