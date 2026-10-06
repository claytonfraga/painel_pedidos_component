package br.projeto.painelpedidos.filtro;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author clayton
 */
public class FiltroTodos implements IFiltroPeriodo {

    @Override
    public boolean aceita(LocalDateTime dataHora, LocalDate hoje) {
        return true;
    }

    @Override
    public LocalDate getDataParaNovoPedido() {
        return null;
    }
}
