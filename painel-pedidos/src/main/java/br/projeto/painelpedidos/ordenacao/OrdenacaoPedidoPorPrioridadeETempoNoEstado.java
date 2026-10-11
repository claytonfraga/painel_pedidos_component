package br.projeto.painelpedidos.ordenacao;

import br.projeto.painelpedidos.model.PedidoAbstract;

/**
 *
 * @author clayton
 */
public final class OrdenacaoPedidoPorPrioridadeETempoNoEstado implements IOrdenacaoPedido {

    @Override
    public int compare(PedidoAbstract primeiro, PedidoAbstract segundo) {
        int comparacaoPrioridade = Boolean.compare(segundo.isPrioridade(), primeiro.isPrioridade());
        if (comparacaoPrioridade != 0) {
            return comparacaoPrioridade;
        }
        return primeiro.getDataHoraMudancaEstado().compareTo(segundo.getDataHoraMudancaEstado());
    }
}
