package br.projeto.painelpedidos.simulacao;

/**
 *
 * @author clayton
 */
public interface ISimuladorNovoPedido {

    void iniciar(ObservadorNovoPedido observador);

    void parar();
}
