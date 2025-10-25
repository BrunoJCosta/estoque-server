package br.com.ms.estoque_server.config;

import java.time.Duration;
import java.util.List;

public class CacheName {

    private static final String ESTOQUE = "estoque";
    public static final String BOOK_ALL = ESTOQUE + "_book_all";
    public static final String BOOK_ID = ESTOQUE + "_book_id";
    public static final String BOOK_REFERENCIA = ESTOQUE + "_book_referencia";

    static List<RedisDTO> cache() {
        return List.of(
                new RedisDTO(BOOK_ALL, Duration.ofMinutes(30)),
                new RedisDTO(BOOK_ID, Duration.ofMinutes(30)),
                new RedisDTO(BOOK_REFERENCIA, Duration.ofMinutes(30))
        );
    }
}
