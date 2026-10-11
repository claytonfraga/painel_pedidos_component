package br.projeto.painelpedidos.repositorio;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author clayton
 */
public final class PedidoRepositoryFake implements IPedidoRepository {

    private final Map<String, PedidoAbstract> pedidosPorIdentificador;
    private int proximoNumeroPedido;

    public PedidoRepositoryFake() {
        this.pedidosPorIdentificador = new LinkedHashMap<>();
        this.proximoNumeroPedido = 1;
    }

    @Override
    public void salvar(PedidoAbstract pedido) {
        pedidosPorIdentificador.put(pedido.getIdentificador(), pedido);
    }

    @Override
    public List<PedidoAbstract> buscarTodos() {
        return new ArrayList<>(pedidosPorIdentificador.values());
    }

    @Override
    public String gerarProximoIdentificador() {
        String identificador = String.valueOf(proximoNumeroPedido);
        proximoNumeroPedido++;
        return identificador;
    }
}
