package br.com.ms.estoque_server.template;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Template {

    private Long referencia;

    private Integer quantidade;

    public final TemplateDTO dto() {
        return new TemplateDTO(referencia, quantidade);
    }
}
