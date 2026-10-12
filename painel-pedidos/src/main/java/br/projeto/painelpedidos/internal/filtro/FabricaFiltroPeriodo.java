package br.projeto.painelpedidos.internal.filtro;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author clayton
 */
public final class FabricaFiltroPeriodo implements IFabricaFiltroPeriodo {

    @Override
    public List<String> rotulos(LocalDate hoje) {
        List<String> rotulos = new ArrayList<>();
        rotulos.add("Hoje (" + hoje.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")");
        rotulos.add("Últimos 7 dias");
        rotulos.add("Últimos 30 dias");
        rotulos.add("Todos");
        rotulos.add("Data específica");
        return rotulos;
    }

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
