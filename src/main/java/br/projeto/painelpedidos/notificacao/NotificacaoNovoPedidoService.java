package br.projeto.painelpedidos.notificacao;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public class NotificacaoNovoPedidoService implements IServicoNotificacaoNovoPedido {

    private final List<RegraChainOfResponsibility> regras;

    public NotificacaoNovoPedidoService(List<RegraChainOfResponsibility> regras) {
        this.regras = Objects.requireNonNull(regras, "regras");
    }

    @Override
    public void processar(ContextoNotificacaoNovoPedido contexto) {
        for (RegraChainOfResponsibility regra : regras) {
            if (regra.seAplica(contexto)) {
                regra.executar(contexto);
            }
        }
    }
}
