package br.com.ms.estoque_server.excecoes;

public class VendaSendoRealizadaException extends Exception {

    public VendaSendoRealizadaException() {
        super("uma venda está sendo realizada");
    }

}
