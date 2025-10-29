package br.com.ms.estoque_server.template;

import br.com.ms.estoque_server.excecoes.NaoEncontradaException;
import br.com.ms.estoque_server.excecoes.QuantidadeAcimaDoLimite;
import br.com.ms.estoque_server.excecoes.VendaSendoRealizadaException;

import java.util.List;
import java.util.Optional;

public interface TemplateService<Entidade extends Template> {

    TemplateRepository<Entidade> repository();

    String tituloEntidade();

    List<TemplateDTO> findAll();

    Optional<TemplateDTO> findById(Long id);

    Optional<TemplateDTO> findByReferencia(Long referencia);

    TemplateDTO criarNovo(TemplateDTO form) throws NaoEncontradaException;

    TemplateDTO registoVenda(TemplateDTO form) throws NaoEncontradaException, QuantidadeAcimaDoLimite, VendaSendoRealizadaException;

    TemplateDTO reabastecer(TemplateDTO form) throws NaoEncontradaException;
}
