package br.com.ms.estoque_server.excecoes;

import java.io.Serial;

public class QuantidadeNaoEncontrada extends NaoEncontradaException {

    @Serial
    private static final long serialVersionUID = -6187175466798615774L;

    public QuantidadeNaoEncontrada(String tipo) {
        super("quantidade do " + tipo);
    }

    public QuantidadeNaoEncontrada(String tipo, boolean masculino) {
        super("quantidade " + (masculino ? "do " : "da ") + tipo, masculino);
    }
}
