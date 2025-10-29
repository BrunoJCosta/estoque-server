package br.com.ms.estoque_server.book;

import br.com.ms.estoque_server.configuration.redis.CacheName;
import br.com.ms.estoque_server.excecoes.*;
import br.com.ms.estoque_server.template.TemplateDTO;
import br.com.ms.estoque_server.template.TemplateRepository;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class BookServiceImpl implements BookService {

    private final BookRepository repository;

    @Override
    public TemplateRepository<Book> repository() {
        return repository;
    }

    @Override
    public String tituloEntidade() {
        return "book";
    }

    @Override
    @Cacheable(cacheNames = CacheName.BOOK_ALL)
    public List<TemplateDTO> findAll() {
        return repository.findAll()
                .stream().map(Book::dto)
                .toList();
    }

    @Override
    @Cacheable(cacheNames = CacheName.BOOK_ID, key = "#id")
    public Optional<TemplateDTO> findById(Long id) {
        return repository.findById(id).map(Book::dto);
    }

    @Override
    @Cacheable(cacheNames = CacheName.BOOK_REFERENCIA, key = "#referencia")
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
    public TemplateDTO registoVenda(TemplateDTO form) throws NaoEncontradaException, QuantidadeAcimaDoLimite {
        form.validar("book");
        Book book = repository.findByReferencia(form.referencia())
                .orElseThrow(BookNaoEncontrado::new);
        book.venda(form.quantidade());
        return repository.saveAndFlush(book).dto();
    }

    @Override
    @CacheEvict(value = {CacheName.BOOK_ID, CacheName.BOOK_ALL, CacheName.BOOK_REFERENCIA}, allEntries = true)
    public TemplateDTO reabastecer(TemplateDTO form) throws NaoEncontradaException {
        form.validar("book");
        Book book = repository.findByReferencia(form.referencia())
                .orElseThrow(BookNaoEncontrado::new);
        book.compra(form.quantidade());
        return repository.saveAndFlush(book).dto();
    }

}