package br.com.ms.estoque_server.controller;

import br.com.ms.estoque_server.book.BookService;
import br.com.ms.estoque_server.excecoes.BookNaoEncontrado;
import br.com.ms.estoque_server.template.TemplateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/estoque")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/id/{id}")
    public ResponseEntity<TemplateDTO> findById(@PathVariable Long id) throws BookNaoEncontrado {
        TemplateDTO templateDTO = bookService.findById(id)
                .orElseThrow(BookNaoEncontrado::new);
        return ResponseEntity.ok(templateDTO);
    }

    @GetMapping("/referencia/{referencia}")
    public ResponseEntity<TemplateDTO> findByReferencia(@PathVariable Long referencia) throws BookNaoEncontrado {
        TemplateDTO templateDTO = bookService.findByReferencia(referencia)
                .orElseThrow(BookNaoEncontrado::new);
        return ResponseEntity.ok(templateDTO);
    }

    @GetMapping()
    public ResponseEntity<List<TemplateDTO>> findAll() {
        List<TemplateDTO> templateDTO = bookService.findAll();
        return ResponseEntity.ok(templateDTO);
    }
}
