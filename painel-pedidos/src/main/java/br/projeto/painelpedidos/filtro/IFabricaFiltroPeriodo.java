package br.projeto.painelpedidos.filtro;

import java.time.LocalDate;

/**
 *
 * @author clayton
 */
public interface IFabricaFiltroPeriodo {

    IFiltroPeriodo criar(int indice, LocalDate dataEspecifica);

    boolean exigeData(int indice);
}
