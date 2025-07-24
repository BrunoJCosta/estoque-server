package br.com.ms.estoque_server.book;

import br.com.ms.estoque_server.excecoes.BookNaoEncontrado;
import br.com.ms.estoque_server.excecoes.NaoEncontradaException;
import br.com.ms.estoque_server.excecoes.QuantidadeAcimaDoLimite;
import br.com.ms.estoque_server.template.TemplateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<TemplateDTO> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(Book::dto)
                .toList();
    }

    @Override
    public Optional<TemplateDTO> findById(Long id) {
        return bookRepository.findById(id).map(Book::dto);
    }

    @Override
    public Optional<TemplateDTO> findByReferencia(Long referenciaId) {
        return bookRepository.findByReferencia(referenciaId).map(Book::dto);
    }

    @Override
    public TemplateDTO criarNovo(TemplateDTO form) throws NaoEncontradaException {
        form.validar("book");
        Book book = new Book();
        book.setReferencia(form.referencia());
        book.setQuantidade(form.quantidade());
        return bookRepository.saveAndFlush(book).dto();
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