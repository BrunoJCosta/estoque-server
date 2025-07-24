package br.com.ms.estoque_server.excecoes;

public class QuantidadeAcimaDoLimite extends Exception {

    public QuantidadeAcimaDoLimite() {
        super("Quantidade acima do limite");
    }
}
