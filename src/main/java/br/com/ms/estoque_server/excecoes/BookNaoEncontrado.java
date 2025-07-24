package br.com.ms.estoque_server.excecoes;

public class BookNaoEncontrado extends NaoEncontradaException {

    public BookNaoEncontrado() {
        super("book");
    }
}
