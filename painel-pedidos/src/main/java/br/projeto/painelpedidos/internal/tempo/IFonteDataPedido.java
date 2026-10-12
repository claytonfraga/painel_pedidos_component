package br.projeto.painelpedidos.internal.tempo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author clayton
 */
public interface IFonteDataPedido {

    void usarDataAtual();

    void usarData(LocalDate data);

    LocalDateTime obterDataHora();
}
