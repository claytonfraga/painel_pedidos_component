package br.projeto.painelpedidos.configuracao;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Adapter sobre a biblioteca dotenv: unico ponto do componente que conhece o
 * arquivo {@code painel_pedidos.env}.
 *
 * <p>O arquivo e OBRIGATORIO no uso do componente, assim como as quatro chaves
 * de SLA por estado (em minutos). Ausentes, lanca excecao.</p>
 *
 * @author clayton
 */
public final class LeitorConfiguracaoPainelPedido {

    private final Dotenv dotenv;

    public LeitorConfiguracaoPainelPedido(String diretorio, String nomeArquivo) {
        Objects.requireNonNull(diretorio, "diretorio");
        Objects.requireNonNull(nomeArquivo, "nomeArquivo");
        try {
            this.dotenv = Dotenv.configure()
                    .directory(diretorio)
                    .filename(nomeArquivo)
                    .load();
        } catch (DotenvException excecao) {
            throw new IllegalStateException(
                    "O arquivo " + nomeArquivo + " e obrigatorio para usar o componente", excecao);
        }
    }

    public ConfiguracaoPainelPedido ler() {
        Map<String, Duration> slaPorSituacao = new LinkedHashMap<>();
        slaPorSituacao.put("Novo", Duration.ofMinutes(lerInteiroObrigatorio("SLA_NOVO_MINUTOS")));
        slaPorSituacao.put("Em preparo", Duration.ofMinutes(lerInteiroObrigatorio("SLA_EM_PREPARO_MINUTOS")));
        slaPorSituacao.put("Pronto para entrega",
                Duration.ofMinutes(lerInteiroObrigatorio("SLA_PRONTO_PARA_ENTREGA_MINUTOS")));
        slaPorSituacao.put("Em rota de entrega",
                Duration.ofMinutes(lerInteiroObrigatorio("SLA_EM_ROTA_DE_ENTREGA_MINUTOS")));

        return new ConfiguracaoPainelPedido(
                slaPorSituacao,
                Duration.ofSeconds(lerInteiro("SLA_MONITOR_INTERVALO_SEGUNDOS", 30)),
                lerInteiro("NOTIFICACAO_DURACAO_SEGUNDOS", 10),
                lerBooleano("SOM_ATIVO", true),
                lerTexto("TITULO_JANELA", "Painel de Acompanhamento de Pedidos"));
    }

    private int lerInteiroObrigatorio(String chave) {
        String valor = dotenv.get(chave);
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalStateException(
                    "A chave " + chave + " e obrigatoria no painel_pedidos.env");
        }
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException excecao) {
            throw new IllegalArgumentException("Valor invalido para " + chave, excecao);
        }
    }

    private int lerInteiro(String chave, int valorPadrao) {
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

    private boolean lerBooleano(String chave, boolean valorPadrao) {
        String valor = dotenv.get(chave);
        if (valor == null || valor.trim().isEmpty()) {
            return valorPadrao;
        }
        return Boolean.parseBoolean(valor.trim());
    }

    private String lerTexto(String chave, String valorPadrao) {
        String valor = dotenv.get(chave);
        if (valor == null || valor.trim().isEmpty()) {
            return valorPadrao;
        }
        return valor.trim();
    }
}
