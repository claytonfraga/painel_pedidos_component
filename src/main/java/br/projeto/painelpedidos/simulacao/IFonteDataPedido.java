package br.projeto.painelpedidos.simulacao;

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
