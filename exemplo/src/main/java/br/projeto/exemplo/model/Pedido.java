package br.projeto.exemplo.model;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.time.LocalDateTime;

/**
 * Pedido concreto do sistema consumidor da atividade. O componente nao possui
 * pedido concreto: o cliente estende {@link PedidoAbstract}.
 *
 * @author clayton
 */
public final class Pedido extends PedidoAbstract {

    public Pedido(
            String identificador,
            LocalDateTime dataHora,
            String nomeCliente,
            String enderecoEntrega,
            double valorTotal) {
        super(identificador, dataHora, nomeCliente, enderecoEntrega, valorTotal);
    }

    @Override
    public PedidoAbstract criarCopia(String novoIdentificador, LocalDateTime novaDataHora) {
        return new Pedido(
                novoIdentificador, novaDataHora, getNomeCliente(), getEnderecoEntrega(), getValorTotal());
    }
}
