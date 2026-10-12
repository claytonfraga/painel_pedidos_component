package br.projeto.exemplo.repositorio;

import br.projeto.painelpedidos.model.PedidoAbstract;
import br.projeto.painelpedidos.repositorio.IPedidoRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Implementacao da atividade, em memoria, da porta de dados do componente. Num
 * sistema real, esta classe seria substituida por um adaptador sobre o banco ou
 * a API do delivery.
 *
 * @author clayton
 */
public final class PedidoEmMemoriaRepository implements IPedidoRepository {

    private final Map<String, PedidoAbstract> pedidosPorIdentificador;
    private int proximoNumeroPedido;

    public PedidoEmMemoriaRepository(List<PedidoAbstract> pedidosIniciais) {
        Objects.requireNonNull(pedidosIniciais, "Informe uma lista de pedidos iniciais valida");
        this.pedidosPorIdentificador = new LinkedHashMap<>();
        this.proximoNumeroPedido = 1;
        for (PedidoAbstract pedido : pedidosIniciais) {
            salvar(pedido);
            int numero = numeroDoIdentificador(pedido);
            if (numero >= proximoNumeroPedido) {
                proximoNumeroPedido = numero + 1;
            }
        }
    }

    @Override
    public void salvar(PedidoAbstract pedido) {
        Objects.requireNonNull(pedido, "Informe um pedido valido");
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

    private int numeroDoIdentificador(PedidoAbstract pedido) {
        try {
            return Integer.parseInt(pedido.getIdentificador());
        } catch (NumberFormatException excecao) {
            throw new IllegalArgumentException(
                    "Informe um identificador de pedido numerico; valor recebido: "
                    + pedido.getIdentificador(), excecao);
        }
    }
}
