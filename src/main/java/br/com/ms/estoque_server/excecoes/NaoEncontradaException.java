package br.com.ms.estoque_server.excecoes;

import java.io.Serial;

public class NaoEncontradaException extends Exception {

    @Serial
    private static final long serialVersionUID = -5465796240001514292L;

    public NaoEncontradaException(String tipo) {
        super(singularMasculino(tipo));
    }

    public NaoEncontradaException(String tipo, boolean masculino) {
        super(masculino ? singularMasculino(tipo) : singularFeminino(tipo));
    }

    public NaoEncontradaException(String tipo, boolean masculino, boolean plural) {
        super(masculino ? singularMasculino(tipo, plural) : singularFeminino(tipo, plural));
    }

    private static String singularMasculino(String tipo) {
        return tipo + " não encontrado";
    }

    private static String singularFeminino(String tipo) {
        return tipo + " não encontrada";
    }

    private static String singularMasculino(String tipo, boolean plural) {
        return plural ? tipo + "não encontrados" : (tipo + " não encontrado");
    }

    private static String singularFeminino(String tipo, boolean plural) {
        return plural ? tipo + "não encontrados" : (tipo + " não encontrada");
    }
}
