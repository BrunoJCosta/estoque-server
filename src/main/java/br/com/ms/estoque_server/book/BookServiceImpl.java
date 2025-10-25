package br.com.ms.estoque_server.book;

import br.com.ms.estoque_server.config.CacheName;
import br.com.ms.estoque_server.excecoes.*;
import br.com.ms.estoque_server.template.TemplateDTO;
import br.com.ms.estoque_server.template.TemplateRepository;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final RedisTemplate<String, String> redisTemplate;
    private final static String LOOK_BOOK = "look:book:";

    @Override
    public TemplateRepository<Book> repository() {
        return repository;
    }

    @Override
    public String tituloEntidade() {
        return "book";
    }

    @Override
    public List<TemplateDTO> findAll() {
        return repository.findAll()
                .stream().map(Book::dto)
                .toList();
    }

    @Override
    @Cacheable(value = CacheName.BOOK_ID, key = "#id")
    public Optional<TemplateDTO> findById(Long id) {
        return repository.findById(id).map(Book::dto);
    }

    @Override
    @Cacheable(value = CacheName.BOOK_REFERENCIA, key = "#referencia")
    public Optional<TemplateDTO> findByReferencia(Long referencia) {
        return repository.findByReferencia(referencia).map(Book::dto);
    }

    @Override
    @CacheEvict(value = {CacheName.BOOK_ID, CacheName.BOOK_ALL, CacheName.BOOK_REFERENCIA}, allEntries = true)
    public TemplateDTO criarNovo(TemplateDTO form) throws NaoEncontradaException {
        String titulo = this.tituloEntidade();
        form.validar(titulo);
        Book template = new Book();
        template.setReferencia(form.referencia());
        template.setQuantidade(form.quantidade());
        return repository.saveAndFlush(template).dto();
    }

    @Override
    @Retry(name = "venda")
    @CacheEvict(value = {CacheName.BOOK_ID, CacheName.BOOK_ALL, CacheName.BOOK_REFERENCIA}, allEntries = true)
    public TemplateDTO registoVenda(TemplateDTO form) throws NaoEncontradaException,
            QuantidadeAcimaDoLimite, VendaSendoRealizadaException {
        String key = LOOK_BOOK + form.referencia();
        String keyBook = redisTemplate.opsForValue().get(key);
        if (keyBook != null)
            throw new VendaSendoRealizadaException();
        redisTemplate.opsForValue().setIfAbsent(key, "", Duration.ofSeconds(3));

        form.validar("book");
        Book book = repository.findByReferencia(form.referencia())
                .orElseThrow(() -> {
                    redisTemplate.delete(key);
                    return new BookNaoEncontrado();
                });
        realizarVenda(form, book, key);
        Book save = repository.saveAndFlush(book);
        redisTemplate.delete(key);
        return save.dto();
    }

    @Override
    @CacheEvict(value = {CacheName.BOOK_ID, CacheName.BOOK_ALL, CacheName.BOOK_REFERENCIA}, allEntries = true)
    public TemplateDTO compra(TemplateDTO form) throws NaoEncontradaException {
        form.validar("book");
        Book book = repository.findByReferencia(form.referencia())
                .orElseThrow(BookNaoEncontrado::new);
        book.compra(form.quantidade());
        return repository.saveAndFlush(book).dto();
    }

    private void realizarVenda(TemplateDTO form, Book book, String key) throws QuantidadeAcimaDoLimite, QuantidadeNaoEncontrada {
        try {
            book.venda(form.quantidade());
        } catch (Exception e) {
            redisTemplate.delete(key);
            throw e;
        }
    }

}