package br.com.ms.estoque_server.configuration.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
@EnableCaching
@Profile("!test")
public class RedisConfiguration {

    @Value("${REDIS}")
    private String hostName;

    @Value("${REDIS_PORT}")
    private Integer port;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(hostName);
        config.setPort(port);
        config.setPassword("bruno_application");
        config.setDatabase(2);

        return new JedisConnectionFactory(config);
    }

    @Bean
    private static RedisCacheManager.RedisCacheManagerBuilder CacheManager(JedisConnectionFactory connectionFactory) {
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(connectionFactory);
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return this::mountedCache;
    }

    private void mountedCache(RedisCacheManager.RedisCacheManagerBuilder builder) {
        CacheName.cache().forEach(cacheName -> {
            RedisCacheConfiguration redisDefault = RedisCacheConfiguration
                    .defaultCacheConfig()
                    .entryTtl(cacheName.duration())
                    .disableCachingNullValues();

            builder.withCacheConfiguration(cacheName.name(),  redisDefault);
        });
    }
}