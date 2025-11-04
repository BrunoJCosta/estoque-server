package br.com.ms.estoque_server.book;

import br.com.ms.estoque_server.configuration.redis.CacheName;
import br.com.ms.estoque_server.excecoes.*;
import br.com.ms.estoque_server.template.TemplateDTO;
import br.com.ms.estoque_server.template.TemplateRepository;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final Environment environment;

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
        String porta = getPorta();
        return repository.findAll()
                .stream().map(b -> b.dto(porta))
                .toList();
    }

    @Override
    public Optional<TemplateDTO> findById(Long id) {
        String porta = getPorta();
        return repository.findById(id).map(b -> b.dto(porta));
    }

    @Override
    @Cacheable(cacheNames = CacheName.BOOK_REFERENCIA, key = "#referencia")
    public Optional<TemplateDTO> findByReferencia(Long referencia) {
        String porta = getPorta();
        return repository.findByReferencia(referencia).map(b  -> b.dto(porta));
    }

    @Override
    @CacheEvict(value = {CacheName.BOOK_ID, CacheName.BOOK_ALL, CacheName.BOOK_REFERENCIA}, allEntries = true)
    public TemplateDTO criarNovo(TemplateDTO form) throws NaoEncontradaException {
        String titulo = this.tituloEntidade();
        form.validar(titulo);
        Book template = new Book();
        template.setReferencia(form.referencia());
        template.setQuantidade(form.quantidade());
        String porta = getPorta();
        return repository.saveAndFlush(template).dto(porta);
    }

    @Override
    @Retry(name = "venda")
    @CacheEvict(value = {CacheName.BOOK_ID, CacheName.BOOK_ALL, CacheName.BOOK_REFERENCIA}, allEntries = true)
    public TemplateDTO registoVenda(TemplateDTO form) throws NaoEncontradaException, QuantidadeAcimaDoLimite {
        form.validar("book");
        Book book = repository.findByReferencia(form.referencia())
                .orElseThrow(BookNaoEncontrado::new);
        book.venda(form.quantidade());
        String porta = getPorta();
        return repository.saveAndFlush(book).dto(porta);
    }

    @Override
    @CacheEvict(value = {CacheName.BOOK_ID, CacheName.BOOK_ALL, CacheName.BOOK_REFERENCIA}, allEntries = true)
    public TemplateDTO reabastecer(TemplateDTO form) throws NaoEncontradaException {
        form.validar("book");
        Book book = repository.findByReferencia(form.referencia())
                .orElseThrow(BookNaoEncontrado::new);
        book.compra(form.quantidade());
        String porta = getPorta();
        return repository.saveAndFlush(book).dto(porta);
    }

    public String getPorta() {
        return environment.getProperty("local.server.port");
    }
}