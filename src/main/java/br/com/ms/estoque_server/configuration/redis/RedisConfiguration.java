package br.com.ms.estoque_server.configuration.redis;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class RedisConfiguration {

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("localhost");
        config.setPort(6379);
        config.setPassword("bruno_application");
        config.setDatabase(2);

        JedisConnectionFactory factory = new JedisConnectionFactory(config);
        // Teste a conexão
        try (Jedis jedis = (Jedis) factory.getConnection().getNativeConnection()) {
            System.out.println("Conexão com Redis bem-sucedida: " + jedis.ping());
        } catch (Exception e) {
            System.err.println("Erro ao conectar ao Redis: " + e.getMessage());
        }
        return factory;
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