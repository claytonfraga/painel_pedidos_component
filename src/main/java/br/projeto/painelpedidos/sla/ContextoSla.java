package br.projeto.painelpedidos.sla;

import br.projeto.painelpedidos.model.Pedido;
import br.projeto.painelpedidos.tempo.ITempoEntregaService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public class ContextoSla {

    private final List<Pedido> pedidos;
    private final LocalDateTime agora;
    private final ITempoEntregaService tempoEntregaService;
    private final List<Pedido> pedidosPriorizados;

    public ContextoSla(List<Pedido> pedidos, LocalDateTime agora, ITempoEntregaService tempoEntregaService) {
        this.pedidos = Objects.requireNonNull(pedidos, "pedidos");
        this.agora = Objects.requireNonNull(agora, "agora");
        this.tempoEntregaService = Objects.requireNonNull(tempoEntregaService, "tempoEntregaService");
        this.pedidosPriorizados = new ArrayList<>();
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public LocalDateTime getAgora() {
        return agora;
    }

    public ITempoEntregaService getTempoEntregaService() {
        return tempoEntregaService;
    }

    public void registrarPriorizado(Pedido pedido) {
        pedidosPriorizados.add(pedido);
    }

    public List<Pedido> getPedidosPriorizados() {
        return pedidosPriorizados;
    }
}
