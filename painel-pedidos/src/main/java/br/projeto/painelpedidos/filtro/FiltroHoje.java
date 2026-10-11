package br.projeto.painelpedidos.filtro;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author clayton
 */
public final class FiltroHoje implements IFiltroPeriodo {

    @Override
    public boolean aceita(LocalDateTime dataHora, LocalDate hoje) {
        return dataHora.toLocalDate().equals(hoje);
    }

    @Override
    public LocalDate getDataParaNovoPedido() {
        return null;
    }
}
