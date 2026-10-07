package br.projeto.painelpedidos.sla;

import br.projeto.painelpedidos.model.Pedido;
import br.projeto.painelpedidos.repositorio.IPedidoRepository;
import br.projeto.painelpedidos.tempo.ITempoEntregaService;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.swing.Timer;

/**
 *
 * @author clayton
 */
public class AgendadorMonitorSla {

    private final IPedidoRepository pedidoRepository;
    private final IMonitorSlaService monitorSlaService;
    private final ITempoEntregaService tempoEntregaService;
    private final Timer timer;

    public AgendadorMonitorSla(
            IPedidoRepository pedidoRepository,
            IMonitorSlaService monitorSlaService,
            ITempoEntregaService tempoEntregaService,
            int intervaloMilissegundos) {

        this.pedidoRepository = Objects.requireNonNull(pedidoRepository, "pedidoRepository");
        this.monitorSlaService = Objects.requireNonNull(monitorSlaService, "monitorSlaService");
        this.tempoEntregaService = Objects.requireNonNull(tempoEntregaService, "tempoEntregaService");
        this.timer = new Timer(intervaloMilissegundos, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                executarMonitoramento();
            }
        });
        this.timer.setInitialDelay(500);
    }

    public void iniciar() {
        timer.start();
    }

    public void parar() {
        timer.stop();
    }

    private void executarMonitoramento() {
        List<Pedido> pedidos = pedidoRepository.buscarTodos();
        ContextoSla contexto = new ContextoSla(pedidos, LocalDateTime.now(), tempoEntregaService);
        monitorSlaService.processar(contexto);
    }
}
