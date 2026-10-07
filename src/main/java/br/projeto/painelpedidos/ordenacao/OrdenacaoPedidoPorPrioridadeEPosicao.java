package br.projeto.painelpedidos.ordenacao;

import br.projeto.painelpedidos.model.Pedido;

/**
 *
 * @author clayton
 */
public class OrdenacaoPedidoPorPrioridadeEPosicao implements IOrdenacaoPedidos {

    @Override
    public int compare(Pedido primeiro, Pedido segundo) {
        int comparacaoPrioridade = Boolean.compare(segundo.isPrioridade(), primeiro.isPrioridade());
        if (comparacaoPrioridade != 0) {
            return comparacaoPrioridade;
        }

        if (primeiro.estaNovo() && segundo.estaNovo()) {
            return segundo.getDataHora().compareTo(primeiro.getDataHora());
        }

        return primeiro.getDataHoraMudancaEstado().compareTo(segundo.getDataHoraMudancaEstado());
    }
}
