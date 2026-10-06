package br.projeto.painelpedidos.ordenacao;

import br.projeto.painelpedidos.model.Pedido;

/**
 *
 * @author clayton
 */
public class OrdenacaoPorTempoNoEstado implements IOrdenacaoPedidos {

    @Override
    public int compare(Pedido primeiro, Pedido segundo) {
        return primeiro.getDataHoraMudancaEstado().compareTo(segundo.getDataHoraMudancaEstado());
    }
}
