package br.com.ms.estoque_server.excecoes;

import java.io.Serial;

public class VendaSendoRealizadaException extends Exception {

    @Serial
    private static final long serialVersionUID = 3258368183631785318L;

    public VendaSendoRealizadaException() {
        super("uma venda está sendo realizada");
    }

}
