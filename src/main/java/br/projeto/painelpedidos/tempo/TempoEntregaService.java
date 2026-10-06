package br.projeto.painelpedidos.tempo;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public class TempoEntregaService implements ITempoEntregaService {

    private final Duration tempoInicial;
    private final Map<String, Duration> mediasPorBairro;
    private final Map<String, Integer> entregasPorBairro;

    public TempoEntregaService(Duration tempoInicial) {
        this.tempoInicial = Objects.requireNonNull(tempoInicial, "tempoInicial");
        this.mediasPorBairro = new LinkedHashMap<>();
        this.entregasPorBairro = new LinkedHashMap<>();
    }

    @Override
    public Duration tempoMedioEntrega(String bairro) {
        Duration media = mediasPorBairro.get(bairro);
        if (media == null) {
            return tempoInicial;
        }
        return media;
    }

    @Override
    public void registrarEntrega(String bairro, Duration duracao) {
        Objects.requireNonNull(bairro, "bairro");
        Objects.requireNonNull(duracao, "duracao");

        int quantidade = entregasPorBairro.getOrDefault(bairro, 0) + 1;
        Duration mediaAtual = tempoMedioEntrega(bairro);
        long mediaMilissegundos = (mediaAtual.toMillis() * (quantidade - 1) + duracao.toMillis()) / quantidade;

        mediasPorBairro.put(bairro, Duration.ofMillis(mediaMilissegundos));
        entregasPorBairro.put(bairro, quantidade);
    }
}
