package br.projeto.painelpedidos.sla;

import br.projeto.painelpedidos.model.PedidoAbstract;
import br.projeto.painelpedidos.repositorio.IPedidoRepository;
import br.projeto.painelpedidos.tempo.IRelogioService;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;
import javax.swing.Timer;

/**
 *
 * @author clayton
 */
public final class AgendadorMonitorSlaService {

    private final IPedidoRepository pedidoRepository;
    private final IMonitorSlaService monitorSlaService;
    private final IRelogioService relogioService;
    private final Timer timer;

    public AgendadorMonitorSlaService(
            IPedidoRepository pedidoRepository,
            IMonitorSlaService monitorSlaService,
            IRelogioService relogioService,
            int intervaloMilissegundos) {

        this.pedidoRepository = Objects.requireNonNull(
                pedidoRepository, "Informe um repositorio de pedidos valido");
        this.monitorSlaService = Objects.requireNonNull(
                monitorSlaService, "Informe um monitor de SLA valido");
        this.relogioService = Objects.requireNonNull(
                relogioService, "Informe um relogio de datas valido");
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
        List<PedidoAbstract> pedidos = pedidoRepository.buscarTodos();
        ContextoSla contexto = new ContextoSla(pedidos, relogioService.agora());
        monitorSlaService.processar(contexto);
    }
}
