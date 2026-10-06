package br.projeto.painelpedidos.sla;

import br.projeto.painelpedidos.model.Pedido;
import br.projeto.painelpedidos.sla.observer.PrioridadeObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public class MonitorSlaService implements IMonitorSlaService {

    private final List<RegraSlaHandler> regras;
    private final List<PrioridadeObserver> observers;

    public MonitorSlaService(List<RegraSlaHandler> regras) {
        this.regras = Objects.requireNonNull(regras, "regras");
        this.observers = new ArrayList<>();
    }

    @Override
    public void adicionarObserver(PrioridadeObserver observer) {
        observers.add(Objects.requireNonNull(observer, "observer"));
    }

    @Override
    public void processar(ContextoSla contexto) {
        for (RegraSlaHandler regra : regras) {
            if (regra.seAplica(contexto)) {
                regra.executar(contexto);
            }
        }

        if (contexto.getPedidosPriorizados().isEmpty()) {
            return;
        }

        List<Pedido> priorizados = new ArrayList<>(contexto.getPedidosPriorizados());
        for (PrioridadeObserver observer : observers) {
            observer.prioridadesAtualizadas(priorizados);
        }
    }
}
