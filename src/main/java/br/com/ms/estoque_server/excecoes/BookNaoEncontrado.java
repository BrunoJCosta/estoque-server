package br.com.ms.estoque_server.excecoes;

import java.io.Serial;

public class BookNaoEncontrado extends NaoEncontradaException {

    @Serial
    private static final long serialVersionUID = -3765644498549323091L;

    public BookNaoEncontrado() {
        super("book");
    }
}
