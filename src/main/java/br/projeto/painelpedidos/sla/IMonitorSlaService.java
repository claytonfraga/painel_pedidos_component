package br.projeto.painelpedidos.sla;

import br.projeto.painelpedidos.sla.observer.PrioridadeObserver;

/**
 *
 * @author clayton
 */
public interface IMonitorSlaService {

    void adicionarObserver(PrioridadeObserver observer);

    void processar(ContextoSla contexto);
}
