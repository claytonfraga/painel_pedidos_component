package br.projeto.painelpedidos.tempo;

import java.time.Duration;

/**
 *
 * @author clayton
 */
public interface ITempoEntregaService {

    Duration tempoMedioEntrega(String bairro);
}
