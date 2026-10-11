package br.projeto.painelpedidos.model;

import java.time.LocalDateTime;

/**
 * Subclasse concreta de apoio aos testes do componente (o componente nao possui
 * pedido concreto).
 *
 * @author clayton
 */
public final class PedidoDeTeste extends PedidoAbstract {

    public PedidoDeTeste(
            String identificador,
            LocalDateTime dataHora,
            String nomeCliente,
            String enderecoEntrega,
            double valorTotal) {
        super(identificador, dataHora, nomeCliente, enderecoEntrega, valorTotal);
    }

    @Override
    public PedidoAbstract criarCopia(String novoIdentificador, LocalDateTime novaDataHora) {
        return new PedidoDeTeste(
                novoIdentificador, novaDataHora, getNomeCliente(), getEnderecoEntrega(), getValorTotal());
    }
}
