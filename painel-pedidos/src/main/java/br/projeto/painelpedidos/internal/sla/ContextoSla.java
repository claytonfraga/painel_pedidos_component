package br.projeto.painelpedidos.internal.sla;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public final class ContextoSla {

    private final List<PedidoAbstract> pedidos;
    private final LocalDateTime agora;
    private final List<PedidoAbstract> pedidosPriorizados;

    public ContextoSla(List<PedidoAbstract> pedidos, LocalDateTime agora) {
        this.pedidos = Objects.requireNonNull(pedidos, "Informe uma lista de pedidos valida");
        this.agora = Objects.requireNonNull(agora, "Informe a data e hora de referencia valida");
        this.pedidosPriorizados = new ArrayList<>();
    }

    public List<PedidoAbstract> getPedidos() {
        return pedidos;
    }

    public LocalDateTime getAgora() {
        return agora;
    }

    public void registrarPriorizado(PedidoAbstract pedido) {
        pedidosPriorizados.add(pedido);
    }

    public List<PedidoAbstract> getPedidosPriorizados() {
        return pedidosPriorizados;
    }
}
