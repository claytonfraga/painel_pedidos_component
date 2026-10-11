package br.projeto.painelpedidos.sla;

/**
 *
 * @author clayton
 */
public interface RegraSlaHandler {

    boolean seAplica(ContextoSla contexto);

    void executar(ContextoSla contexto);
}
