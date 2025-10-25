package br.com.ms.estoque_server.excecoes;

import java.io.Serial;

public class QuantidadeAcimaDoLimite extends Exception {

    @Serial
    private static final long serialVersionUID = 785952498287024L;

    public QuantidadeAcimaDoLimite() {
        super("Quantidade acima do limite");
    }
}
