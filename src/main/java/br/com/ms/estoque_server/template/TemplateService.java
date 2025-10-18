package br.com.ms.estoque_server.template;

import br.com.ms.estoque_server.excecoes.NaoEncontradaException;
import br.com.ms.estoque_server.excecoes.QuantidadeAcimaDoLimite;
import br.com.ms.estoque_server.excecoes.VendaSendoRealizadaException;

import java.util.List;
import java.util.Optional;

public interface TemplateService<Entidade extends Template> {

    TemplateRepository<Entidade> repository();

    Entidade newEntity();

    String tituloEntidade();

    default List<TemplateDTO> findAll() {
        return repository().findAll()
                .stream().map(Entidade::dto)
                .toList();
    }

    default Optional<TemplateDTO> findById(Long id) {
        return repository().findById(id).map(Entidade::dto);
    }

    default Optional<TemplateDTO> findByReferencia(Long referencia) {
        return repository().findByReferencia(referencia).map(Entidade::dto);
    }

    default TemplateDTO criarNovo(TemplateDTO form) throws NaoEncontradaException {
        String titulo = this.tituloEntidade();
        form.validar(titulo);
        Entidade template = this.newEntity();
        template.setReferencia(form.referencia());
        template.setQuantidade(form.quantidade());
        return repository().saveAndFlush(template).dto();
    }

    TemplateDTO registoVenda(TemplateDTO form) throws NaoEncontradaException, QuantidadeAcimaDoLimite, VendaSendoRealizadaException;

    TemplateDTO compra(TemplateDTO form) throws NaoEncontradaException;
}
