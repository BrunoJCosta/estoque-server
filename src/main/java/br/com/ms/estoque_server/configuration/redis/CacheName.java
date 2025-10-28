package br.com.ms.estoque_server.configuration.redis;

import java.time.Duration;
import java.util.List;

public class CacheName {

    public static final String BOOK_ALL = "estoque_book_all";
    public static final String BOOK_ID = "estoque_book_id";
    public static final String BOOK_REFERENCIA = "estoque_book_referencia";

    static List<RedisDTO> cache() {
        return List.of(
                new RedisDTO(BOOK_ALL, Duration.ofMinutes(30)),
                new RedisDTO(BOOK_ID, Duration.ofMinutes(30)),
                new RedisDTO(BOOK_REFERENCIA, Duration.ofMinutes(30))
        );
    }
}
