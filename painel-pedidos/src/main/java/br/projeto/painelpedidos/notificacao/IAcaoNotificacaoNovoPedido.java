package br.projeto.painelpedidos.notificacao;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.util.List;

/**
 *
 * @author clayton
 */
public interface IAcaoNotificacaoNovoPedido {

    void salvarPedidos(List<PedidoAbstract> pedidos);

    void atualizarPainel();

    void selecionarFocar(PedidoAbstract pedido);

    void emitirAlertaSonoro();

    void exibirMensagem(String mensagem);

    void destacarPedidos(List<PedidoAbstract> pedidos);
}
