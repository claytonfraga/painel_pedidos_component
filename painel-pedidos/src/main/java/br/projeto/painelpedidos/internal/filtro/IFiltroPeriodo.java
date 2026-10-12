package br.projeto.painelpedidos.internal.filtro;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author clayton
 */
public interface IFiltroPeriodo {

    boolean aceita(LocalDateTime dataHora, LocalDate hoje);

    LocalDate getDataParaNovoPedido();
}
