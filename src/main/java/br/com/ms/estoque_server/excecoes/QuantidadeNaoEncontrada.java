package br.com.ms.estoque_server.excecoes;

public class QuantidadeNaoEncontrada extends NaoEncontradaException {

    public QuantidadeNaoEncontrada(String tipo) {
        super("quantidade do " + tipo);
    }

    public QuantidadeNaoEncontrada(String tipo, boolean masculino) {
        super("quantidade " + (masculino ? "do " : "da ") + tipo, masculino);
    }
}
