package br.projeto.painelpedidos.internal.filtro;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author clayton
 */
public final class FiltroCompleto implements IFiltroPeriodo {

    @Override
    public boolean aceita(LocalDateTime dataHora, LocalDate hoje) {
        return true;
    }

    @Override
    public LocalDate getDataParaNovoPedido() {
        return null;
    }
}
