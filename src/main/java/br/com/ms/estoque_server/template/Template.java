package br.com.ms.estoque_server.template;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Template {

    private Long referencia;

    private Integer quantidade;

    public TemplateDTO dto() {
        return new TemplateDTO(referencia, quantidade);
    }
}
