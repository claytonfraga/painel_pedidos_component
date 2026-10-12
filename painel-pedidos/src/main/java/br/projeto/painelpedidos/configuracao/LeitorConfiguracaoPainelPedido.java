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
        Objects.requireNonNull(diretorio, "Informe um diretorio do arquivo de configuracao valido");
        Objects.requireNonNull(nomeArquivo, "Informe um nome de arquivo de configuracao valido");
        try {
            this.dotenv = Dotenv.configure()
                    .directory(diretorio)
                    .filename(nomeArquivo)
                    .load();
        } catch (DotenvException excecao) {
            throw new IllegalStateException(
                    "O arquivo " + nomeArquivo + " e obrigatorio em " + diretorio
                    + "; crie-o com as chaves de SLA obrigatorias", excecao);
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
                lerTexto("TITULO_JANELA", "Painel de Acompanhamento de Pedidos"),
                lerTextoObrigatorio("IDENTIFICADOR_APLICACAO"));
    }

    private String lerTextoObrigatorio(String chave) {
        String valor = dotenv.get(chave);
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalStateException(
                    "Informe a chave " + chave + " no painel_pedidos.env "
                    + "(identificador da aplicacao; distingue este painel dos demais na maquina)");
        }
        return valor.trim();
    }

    private int lerInteiroObrigatorio(String chave) {
        String valor = dotenv.get(chave);
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalStateException(
                    "Informe a chave " + chave + " no painel_pedidos.env (valor inteiro em minutos)");
        }
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException excecao) {
            throw new IllegalArgumentException(
                    "Informe um valor inteiro valido para " + chave + " no painel_pedidos.env", excecao);
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
            throw new IllegalArgumentException(
                    "Informe um valor inteiro valido para " + chave + " no painel_pedidos.env", excecao);
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
