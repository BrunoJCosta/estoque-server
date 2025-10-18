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
    public TemplateDTO registoVenda(TemplateDTO form) throws NaoEncontradaException, QuantidadeAcimaDoLimite {
        form.validar("book");
        Book book = bookRepository.findByReferencia(form.referencia())
                .orElseThrow(BookNaoEncontrado::new);
        book.venda(form.quantidade());
        return bookRepository.saveAndFlush(book).dto();
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