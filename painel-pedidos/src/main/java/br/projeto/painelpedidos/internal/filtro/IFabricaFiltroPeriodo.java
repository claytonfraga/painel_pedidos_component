package br.projeto.painelpedidos.internal.filtro;

import java.time.LocalDate;

/**
 *
 * @author clayton
 */
public interface IFabricaFiltroPeriodo {

    IFiltroPeriodo criar(int indice, LocalDate dataEspecifica);

    boolean exigeData(int indice);
}
