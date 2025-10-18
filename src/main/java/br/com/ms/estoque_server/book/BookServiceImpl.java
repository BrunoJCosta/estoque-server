package br.com.ms.estoque_server.book;

import br.com.ms.estoque_server.excecoes.*;
import br.com.ms.estoque_server.template.Template;
import br.com.ms.estoque_server.template.TemplateDTO;
import br.com.ms.estoque_server.template.TemplateRepository;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final static String LOOK_BOOK = "look:book:";

    @Override
    public TemplateRepository<Book> repository() {
        return bookRepository;
    }

    @Override
    public Template newEntity() {
        return new Book();
    }

    @Override
    public String tituloEntidade() {
        return "book";
    }

    @Override
    @Retry(name = "venda")
    public TemplateDTO registoVenda(TemplateDTO form) throws NaoEncontradaException,
            QuantidadeAcimaDoLimite, VendaSendoRealizadaException {
        String key = LOOK_BOOK + form.referencia();
        String keyBook = redisTemplate.opsForValue().get(key);
        if (keyBook != null)
            throw new VendaSendoRealizadaException();
        redisTemplate.opsForValue().setIfAbsent(key,"", Duration.ofSeconds(3));

        form.validar("book");
        Book book = bookRepository.findByReferencia(form.referencia())
                .orElseThrow(() -> {
                    redisTemplate.delete(key);
                    return new BookNaoEncontrado();
                });
        realizarVenda(form, book, key);
        Book save = bookRepository.saveAndFlush(book);
        redisTemplate.delete(key);
        return save.dto();
    }

    private void realizarVenda(TemplateDTO form, Book book, String key) throws QuantidadeAcimaDoLimite, QuantidadeNaoEncontrada {
        try {
            book.venda(form.quantidade());
        } catch (Exception e) {
            redisTemplate.delete(key);
            throw e;
        }
    }

    @Override
    public TemplateDTO compra(TemplateDTO form) throws NaoEncontradaException {
        form.validar("book");
        Book book = bookRepository.findByReferencia(form.referencia())
                .orElseThrow(BookNaoEncontrado::new);
        book.compra(form.quantidade());
        return bookRepository.saveAndFlush(book).dto();
    }

}