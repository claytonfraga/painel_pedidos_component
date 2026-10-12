package br.projeto.painelpedidos.filtro;

import java.time.LocalDate;

/**
 *
 * @author clayton
 */
public final class FabricaFiltroPeriodo implements IFabricaFiltroPeriodo {

    @Override
    public IFiltroPeriodo criar(int indice, LocalDate dataEspecifica) {
        if (indice == 0) {
            return new FiltroHoje();
        }
        if (indice == 1) {
            return new FiltroSemana();
        }
        if (indice == 2) {
            return new FiltroMes();
        }
        if (indice == 3) {
            return new FiltroCompleto();
        }
        if (indice == 4) {
            if (dataEspecifica == null) {
                throw new IllegalArgumentException("Informe uma data valida (dd/MM/yyyy)");
            }
            return new FiltroDataEspecifica(dataEspecifica);
        }
        throw new IllegalArgumentException(
                "Escolha uma opcao de periodo valida (o indice " + indice + " nao existe)");
    }

    @Override
    public boolean exigeData(int indice) {
        return indice == 4;
    }
}
