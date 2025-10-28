package br.com.ms.estoque_server.template;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

public abstract class Template implements Serializable {

    @Serial
    private static final long serialVersionUID = 6845463742916215768L;

    public abstract Long templateReference();

    public abstract Integer templateQuantidade();

    public final TemplateDTO dto() {
        Long reference = this.templateReference();
        Integer quantidade = this.templateQuantidade();
        return new TemplateDTO(reference, quantidade);
    }
}
