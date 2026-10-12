package br.projeto.painelpedidos.internal.filtro;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author clayton
 */
final class FiltroDataEspecifica implements IFiltroPeriodo {

    private final LocalDate data;

    public FiltroDataEspecifica(LocalDate data) {
        this.data = Objects.requireNonNull(data, "Informe uma data valida");
    }

    @Override
    public boolean aceita(LocalDateTime dataHora, LocalDate hoje) {
        return dataHora.toLocalDate().equals(data);
    }

    @Override
    public LocalDate getDataParaNovoPedido() {
        return data;
    }
}
