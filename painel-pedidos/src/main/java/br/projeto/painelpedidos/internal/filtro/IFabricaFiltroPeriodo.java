package br.projeto.painelpedidos.internal.filtro;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author clayton
 */
public interface IFabricaFiltroPeriodo {

    /**
     * Rotulos dos periodos, na ordem em que o indice de {@link #criar(int, LocalDate)}
     * e {@link #exigeData(int)} os esperam. E a fonte unica da lista: a tela
     * monta o combo daqui, para a ordem nao divergir do mapeamento.
     */
    List<String> rotulos(LocalDate hoje);

    IFiltroPeriodo criar(int indice, LocalDate dataEspecifica);

    boolean exigeData(int indice);
}
