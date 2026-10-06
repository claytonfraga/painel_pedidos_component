package br.projeto.painelpedidos.configuracao;

import io.github.cdimascio.dotenv.Dotenv;

/**
 *
 * @author clayton
 */
public class LeitorEnv {

    private final Dotenv dotenv;

    public LeitorEnv() {
        this.dotenv = Dotenv.configure().ignoreIfMissing().load();
    }

    public int lerInteiro(String chave, int valorPadrao) {
        String valor = dotenv.get(chave);
        if (valor == null || valor.trim().isEmpty()) {
            return valorPadrao;
        }
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException excecao) {
            throw new IllegalArgumentException("Valor invalido para " + chave, excecao);
        }
    }
}
