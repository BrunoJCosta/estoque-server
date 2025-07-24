package br.com.ms.estoque_server.excecoes;

public class ReferenciaNaoEncontrada extends NaoEncontradaException {

    public ReferenciaNaoEncontrada(String tipo) {
        super("referencia do " + tipo);
    }

    public ReferenciaNaoEncontrada(String tipo, boolean masculino) {
        super("referencia " + (masculino ? "do " : "da ") + tipo);
    }
}
