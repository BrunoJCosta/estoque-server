package br.com.ms.estoque_server.config;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisConfiguration {

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("localhost");
        config.setPort(6379);
        config.setPassword("bruno_application");
        config.setDatabase(3);

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