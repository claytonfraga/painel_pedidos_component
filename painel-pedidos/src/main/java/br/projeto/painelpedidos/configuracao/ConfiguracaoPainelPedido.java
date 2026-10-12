package br.projeto.painelpedidos.configuracao;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Configuracao do componente. Imutavel; construida pelo
 * {@link LeitorConfiguracaoPainelPedido} a partir do arquivo
 * {@code painel_pedidos.env} (obrigatorio).
 *
 * <p>O SLA e fixo por estado, em minutos, e vive num unico mapa
 * (situacao -> duracao): novos SLAs entram como novas entradas, sem alterar o
 * contrato desta classe (OCP).</p>
 *
 * @author clayton
 */
public final class ConfiguracaoPainelPedido {

    private final Map<String, Duration> slaPorSituacao;
    private final Duration slaMonitorIntervalo;
    private final int duracaoNotificacaoSegundos;
    private final boolean somAtivo;
    private final String tituloJanela;
    private final String identificadorAplicacao;

    public ConfiguracaoPainelPedido(
            Map<String, Duration> slaPorSituacao,
            Duration slaMonitorIntervalo,
            int duracaoNotificacaoSegundos,
            boolean somAtivo,
            String tituloJanela,
            String identificadorAplicacao) {

        this.slaPorSituacao = copiarSlas(slaPorSituacao);
        this.slaMonitorIntervalo = validarDuracao(
                slaMonitorIntervalo, "a duracao do intervalo do monitor de SLA");
        if (duracaoNotificacaoSegundos < 1) {
            throw new IllegalArgumentException(
                    "Informe a duracao da notificacao em segundos com pelo menos 1");
        }
        String titulo = validarTextoObrigatorio(tituloJanela, "um titulo de janela valido");

        this.duracaoNotificacaoSegundos = duracaoNotificacaoSegundos;
        this.somAtivo = somAtivo;
        this.tituloJanela = titulo;
        this.identificadorAplicacao = validarTextoObrigatorio(
                identificadorAplicacao, "um identificador da aplicacao valido");
    }

    public Map<String, Duration> getSlaPorSituacao() {
        return slaPorSituacao;
    }

    public Duration getSlaMonitorIntervalo() {
        return slaMonitorIntervalo;
    }

    public int getDuracaoNotificacaoSegundos() {
        return duracaoNotificacaoSegundos;
    }

    public boolean isSomAtivo() {
        return somAtivo;
    }

    public String getTituloJanela() {
        return tituloJanela;
    }

    public String getIdentificadorAplicacao() {
        return identificadorAplicacao;
    }

    private Map<String, Duration> copiarSlas(Map<String, Duration> slaPorSituacao) {
        Objects.requireNonNull(slaPorSituacao, "Informe um mapa de SLA por situacao valido");
        if (slaPorSituacao.isEmpty()) {
            throw new IllegalArgumentException(
                    "Informe ao menos uma situacao de SLA com a sua duracao");
        }
        Map<String, Duration> copia = new LinkedHashMap<>();
        for (Map.Entry<String, Duration> entrada : slaPorSituacao.entrySet()) {
            String situacao = Objects.requireNonNull(
                    entrada.getKey(), "Informe uma situacao de SLA valida").trim();
            if (situacao.isEmpty()) {
                throw new IllegalArgumentException("Informe uma situacao de SLA nao vazia");
            }
            copia.put(situacao, validarDuracao(
                    entrada.getValue(), "a duracao do SLA de " + situacao));
        }
        return Collections.unmodifiableMap(copia);
    }

    private Duration validarDuracao(Duration duracao, String descricao) {
        Objects.requireNonNull(duracao, "Informe " + descricao);
        if (duracao.isZero() || duracao.isNegative()) {
            throw new IllegalArgumentException(
                    "Informe " + descricao + " com valor maior que zero");
        }
        return duracao;
    }

    private String validarTextoObrigatorio(String valor, String descricao) {
        String texto = Objects.requireNonNull(valor, "Informe " + descricao).trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("Informe " + descricao + " (texto nao vazio)");
        }
        return texto;
    }
}
