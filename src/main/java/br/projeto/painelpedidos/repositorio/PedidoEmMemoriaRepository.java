package br.projeto.painelpedidos.repositorio;

import br.projeto.painelpedidos.model.Pedido;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public class PedidoEmMemoriaRepository implements IPedidoRepository {

    private final Map<String, Pedido> pedidosPorIdentificador;
    private int proximoNumeroPedido;

    public PedidoEmMemoriaRepository(List<Pedido> pedidosIniciais) {
        Objects.requireNonNull(pedidosIniciais, "pedidosIniciais");
        this.pedidosPorIdentificador = new LinkedHashMap<>();
        this.proximoNumeroPedido = 1;
        for (Pedido pedido : pedidosIniciais) {
            salvar(pedido);
            int numero = numeroDoIdentificador(pedido);
            if (numero >= proximoNumeroPedido) {
                proximoNumeroPedido = numero + 1;
            }
        }
    }

    @Override
    public void salvar(Pedido pedido) {
        Objects.requireNonNull(pedido, "pedido");
        pedidosPorIdentificador.put(pedido.getIdentificador(), pedido);
    }

    @Override
    public List<Pedido> buscarTodos() {
        return new ArrayList<>(pedidosPorIdentificador.values());
    }

    @Override
    public String gerarProximoIdentificador() {
        String identificador = String.valueOf(proximoNumeroPedido);
        proximoNumeroPedido++;
        return identificador;
    }

    private int numeroDoIdentificador(Pedido pedido) {
        try {
            return Integer.parseInt(pedido.getIdentificador());
        } catch (NumberFormatException excecao) {
            throw new IllegalArgumentException(
                    "Identificador de pedido nao numerico: " + pedido.getIdentificador(), excecao);
        }
    }
}
