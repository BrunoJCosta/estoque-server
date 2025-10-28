package br.com.ms.estoque_server.book;

import br.com.ms.estoque_server.excecoes.QuantidadeAcimaDoLimite;
import br.com.ms.estoque_server.excecoes.QuantidadeNaoEncontrada;
import br.com.ms.estoque_server.template.Template;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Entity
@Table(name = "book", schema = "estoque")
@Getter
class Book extends Template {

    @Serial
    private static final long serialVersionUID = -4577370996102597469L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", columnDefinition = "bigserial")
    private Long id;

    @Column(name = "referencia", columnDefinition = "bigint")
    @Setter
    private Long referencia;

    @Column(name = "quantidade")
    @Setter
    private Integer quantidade;

    void venda(Integer quantidade) throws QuantidadeAcimaDoLimite, QuantidadeNaoEncontrada {
        if (quantidade <= 0) {
            throw new QuantidadeNaoEncontrada("book");
        }
        if (this.quantidade < quantidade) {
            throw new QuantidadeAcimaDoLimite();
        }
        this.quantidade -= quantidade;
    }

    void compra(Integer quantidade) throws QuantidadeNaoEncontrada {
        if (quantidade <= 0 )
            throw new QuantidadeNaoEncontrada("book");
        this.quantidade += quantidade;
    }

    @Override
    public Long templateReference() {
        return this.referencia;
    }

    @Override
    public Integer templateQuantidade() {
        return this.quantidade;
    }
}
