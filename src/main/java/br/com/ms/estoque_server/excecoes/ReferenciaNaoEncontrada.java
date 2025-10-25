package br.com.ms.estoque_server.excecoes;

import java.io.Serial;

public class ReferenciaNaoEncontrada extends NaoEncontradaException {

    @Serial
    private static final long serialVersionUID = -4230535370416885813L;

    public ReferenciaNaoEncontrada(String tipo) {
        super("referencia do " + tipo);
    }

    public ReferenciaNaoEncontrada(String tipo, boolean masculino) {
        super("referencia " + (masculino ? "do " : "da ") + tipo);
    }
}
