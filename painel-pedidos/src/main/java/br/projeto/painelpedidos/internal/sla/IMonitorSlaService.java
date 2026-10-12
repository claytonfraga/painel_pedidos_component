package br.projeto.painelpedidos.internal.sla;

import br.projeto.painelpedidos.internal.sla.observer.PrioridadeObserver;

/**
 *
 * @author clayton
 */
public interface IMonitorSlaService {

    void adicionarObserver(PrioridadeObserver observer);

    void processar(ContextoSla contexto);
}
