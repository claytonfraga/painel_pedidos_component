package br.projeto.painelpedidos.filtro;

import java.time.LocalDate;

/**
 *
 * @author clayton
 */
public class FabricaFiltroPeriodo implements IFabricaFiltroPeriodo {

    @Override
    public IFiltroPeriodo criar(int indice, LocalDate dataEspecifica) {
        if (indice == 0) {
            return new FiltroHoje();
        }
        if (indice == 1) {
            return new FiltroUltimos7Dias();
        }
        if (indice == 2) {
            return new FiltroUltimos30Dias();
        }
        if (indice == 3) {
            return new FiltroTodos();
        }
        if (indice == 4) {
            if (dataEspecifica == null) {
                throw new IllegalArgumentException("Informe uma data valida (dd/MM/yyyy)");
            }
            return new FiltroDataEspecifica(dataEspecifica);
        }
        throw new IllegalArgumentException("Opcao de periodo invalida: " + indice);
    }

    @Override
    public boolean exigeData(int indice) {
        return indice == 4;
    }
}
