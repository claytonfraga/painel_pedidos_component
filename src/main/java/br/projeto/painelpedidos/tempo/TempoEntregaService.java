package br.projeto.painelpedidos.tempo;

import java.time.Duration;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public class TempoEntregaService implements ITempoEntregaService {

    private final Duration tempoInicial;

    public TempoEntregaService(Duration tempoInicial) {
        this.tempoInicial = Objects.requireNonNull(tempoInicial, "tempoInicial");
    }

    @Override
    public Duration tempoMedioEntrega(String bairro) {
        return tempoInicial;
    }
}
