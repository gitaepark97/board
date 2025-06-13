package board.backend.common.infra;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
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
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.username}")
    private String username;

    @Value("${spring.data.redis.password}")
    private String password;

    static ObjectMapper redisObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ISO-8601 형식
        mapper.activateDefaultTyping(
            LaissezFaireSubTypeValidator.instance,
            ObjectMapper.DefaultTyping.EVERYTHING,
            JsonTypeInfo.As.PROPERTY
        );
        return mapper;
    }

    @Bean
    @Primary
    RedisTemplate<String, Object> redisTemplate() {
        LettuceConnectionFactory factory = createFactory(host, port, username, password, 0);
        return getStringObjectRedisTemplate(factory);
    }

    @Bean
    @Qualifier("cacheRedisTemplate")
    RedisTemplate<String, Object> cacheRedisTemplate() {
        LettuceConnectionFactory factory = createFactory(host, port, username, password, 2);
        return getStringObjectRedisTemplate(factory);
    }

    @Bean
    @Primary
    StringRedisTemplate stringRedisTemplate() {
        LettuceConnectionFactory factory = createFactory(host, port, username, password, 0);
        return getStringRedisTemplate(factory);
    }

    @Bean
    @Qualifier("lockRedisTemplate")
    StringRedisTemplate lockRedisTemplate() {
        LettuceConnectionFactory factory = createFactory(host, port, username, password, 1);
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
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(redisObjectMapper()));
        template.afterPropertiesSet();

        return template;
    }

    private LettuceConnectionFactory createFactory(
        String host,
        int port,
        String username,
        String password,
        int dbIndex
    ) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        config.setUsername(username);
        config.setPassword(RedisPassword.of(password));
        config.setDatabase(dbIndex);

        return new LettuceConnectionFactory(config);
    }

}
