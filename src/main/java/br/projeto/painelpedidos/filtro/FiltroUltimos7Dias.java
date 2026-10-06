package br.projeto.painelpedidos.filtro;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author clayton
 */
public class FiltroUltimos7Dias implements IFiltroPeriodo {

    @Override
    public boolean aceita(LocalDateTime dataHora, LocalDate hoje) {
        LocalDate data = dataHora.toLocalDate();
        return !data.isBefore(hoje.minusDays(6)) && !data.isAfter(hoje);
    }

    @Override
    public LocalDate getDataParaNovoPedido() {
        return null;
    }
}
