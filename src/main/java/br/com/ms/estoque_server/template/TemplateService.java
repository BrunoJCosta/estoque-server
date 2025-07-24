package br.com.ms.estoque_server.template;

import br.com.ms.estoque_server.excecoes.BookNaoEncontrado;
import br.com.ms.estoque_server.excecoes.NaoEncontradaException;
import br.com.ms.estoque_server.excecoes.QuantidadeAcimaDoLimite;

import java.util.List;
import java.util.Optional;

public interface TemplateService {

    List<TemplateDTO> findAll();

    Optional<TemplateDTO> findById(Long id);

    Optional<TemplateDTO> findByReferencia(Long referencia) throws BookNaoEncontrado;

    TemplateDTO criarNovo(TemplateDTO form) throws NaoEncontradaException;

    TemplateDTO registoVenda(TemplateDTO form) throws NaoEncontradaException, QuantidadeAcimaDoLimite;

    TemplateDTO compra(TemplateDTO form) throws NaoEncontradaException;
}
