package br.projeto.painelpedidos.simulacao;

import br.projeto.painelpedidos.simulacao.observer.NovoPedidoObserver;

/**
 *
 * @author clayton
 */
public interface ISimuladorNovoPedido {

    void iniciar(NovoPedidoObserver observador);

    void parar();
}
