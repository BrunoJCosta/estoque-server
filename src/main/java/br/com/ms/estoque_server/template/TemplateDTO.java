package br.com.ms.estoque_server.template;

import br.com.ms.estoque_server.excecoes.NaoEncontradaException;
import br.com.ms.estoque_server.excecoes.QuantidadeNaoEncontrada;
import br.com.ms.estoque_server.excecoes.ReferenciaNaoEncontrada;

import java.io.Serializable;
import java.util.Objects;

public record TemplateDTO(Long referencia, Integer quantidade, String environment) implements Serializable {

    public void validar(String tipo) throws NaoEncontradaException {
        this.validar(tipo, true);
    }

    public void validar(String tipo, boolean masculino) throws NaoEncontradaException {
        if (Objects.isNull(referencia))
            throw new ReferenciaNaoEncontrada(tipo, masculino);
        if (Objects.isNull(quantidade))
            throw new QuantidadeNaoEncontrada(tipo, masculino);
    }

}
