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

    public ConfiguracaoPainelPedido(
            Map<String, Duration> slaPorSituacao,
            Duration slaMonitorIntervalo,
            int duracaoNotificacaoSegundos,
            boolean somAtivo,
            String tituloJanela) {

        this.slaPorSituacao = copiarSlas(slaPorSituacao);
        this.slaMonitorIntervalo = validarDuracao(slaMonitorIntervalo, "slaMonitorIntervalo");
        if (duracaoNotificacaoSegundos < 1) {
            throw new IllegalArgumentException("Duracao da notificacao deve ser de pelo menos 1 segundo");
        }
        String titulo = Objects.requireNonNull(tituloJanela, "tituloJanela").trim();
        if (titulo.isEmpty()) {
            throw new IllegalArgumentException("tituloJanela deve ser informado");
        }

        this.duracaoNotificacaoSegundos = duracaoNotificacaoSegundos;
        this.somAtivo = somAtivo;
        this.tituloJanela = titulo;
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

    private Map<String, Duration> copiarSlas(Map<String, Duration> slaPorSituacao) {
        Objects.requireNonNull(slaPorSituacao, "slaPorSituacao");
        if (slaPorSituacao.isEmpty()) {
            throw new IllegalArgumentException("slaPorSituacao deve ter ao menos uma situacao");
        }
        Map<String, Duration> copia = new LinkedHashMap<>();
        for (Map.Entry<String, Duration> entrada : slaPorSituacao.entrySet()) {
            String situacao = Objects.requireNonNull(entrada.getKey(), "situacao").trim();
            if (situacao.isEmpty()) {
                throw new IllegalArgumentException("situacao de SLA deve ser informada");
            }
            copia.put(situacao, validarDuracao(entrada.getValue(), "sla de " + situacao));
        }
        return Collections.unmodifiableMap(copia);
    }

    private Duration validarDuracao(Duration duracao, String nomeCampo) {
        Objects.requireNonNull(duracao, nomeCampo);
        if (duracao.isZero() || duracao.isNegative()) {
            throw new IllegalArgumentException(nomeCampo + " deve ser maior que zero");
        }
        return duracao;
    }
}
