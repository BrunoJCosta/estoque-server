package br.com.ms.estoque_server.book;

import br.com.ms.estoque_server.configuration.redis.CacheName;
import br.com.ms.estoque_server.template.TemplateRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface BookRepository extends TemplateRepository<Book>, JpaRepository<Book, Long> {

    @Override
    @Cacheable(value = CacheName.BOOK_REFERENCIA, key = "#id")
    Optional<Book> findByReferencia(Long id);

    @Override
    @SuppressWarnings("NullableProblems")
    @Cacheable(value = CacheName.BOOK_ID)
    Optional<Book> findById(Long id);
}