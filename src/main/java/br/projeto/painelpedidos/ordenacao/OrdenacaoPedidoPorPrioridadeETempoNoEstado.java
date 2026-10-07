package br.projeto.painelpedidos.ordenacao;

import br.projeto.painelpedidos.model.Pedido;

/**
 *
 * @author clayton
 */
public class OrdenacaoPedidoPorPrioridadeETempoNoEstado implements IOrdenacaoPedidos {

    @Override
    public int compare(Pedido primeiro, Pedido segundo) {
        int comparacaoPrioridade = Boolean.compare(segundo.isPrioridade(), primeiro.isPrioridade());
        if (comparacaoPrioridade != 0) {
            return comparacaoPrioridade;
        }
        return primeiro.getDataHoraMudancaEstado().compareTo(segundo.getDataHoraMudancaEstado());
    }
}
