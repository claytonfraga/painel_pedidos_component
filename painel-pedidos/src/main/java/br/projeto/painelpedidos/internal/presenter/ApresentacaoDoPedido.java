package br.projeto.painelpedidos.internal.presenter;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.awt.Color;

/**
 * Aparencia do pedido por situacao: texto da acao principal, cor de fundo da
 * raia e rotulo/cor do status. Concentra num tipo coeso o que antes eram quatro
 * cadeias de verificacao de estado dentro do presenter.
 *
 * @author clayton
 */
final class ApresentacaoDoPedido {

    String textoDaAcaoPrincipal(PedidoAbstract pedido) {
        if (pedido.estaNovo()) {
            return "Confirmar pagamento";
        }
        if (pedido.estaEmPreparo()) {
            return "Pronto pra entrega";
        }
        if (pedido.estaProntoParaEntrega()) {
            return "Sair pra entrega";
        }
        if (pedido.estaEmRotaDeEntrega()) {
            return "Entregue";
        }
        if (pedido.estaCancelado()) {
            return "Criar novo pedido";
        }
        return "Executar ação";
    }

    Color corDeFundoDaRaia(PedidoAbstract pedido) {
        if (pedido.estaNovo()) {
            return new Color(232, 239, 247);
        }
        if (pedido.estaEmPreparo()) {
            return new Color(218, 236, 252);
        }
        if (pedido.estaProntoParaEntrega()) {
            return new Color(255, 242, 196);
        }
        if (pedido.estaEmRotaDeEntrega()) {
            return new Color(252, 228, 209);
        }
        if (pedido.estaEntregue()) {
            return new Color(222, 242, 222);
        }
        if (pedido.estaCancelado()) {
            return new Color(249, 221, 221);
        }
        return Color.WHITE;
    }

    String textoDoStatus(PedidoAbstract pedido) {
        if (pedido.estaCancelado()) {
            return "Pedido cancelado";
        }
        if (pedido.isPagamentoRegistrado()) {
            return "Pagamento confirmado";
        }
        return "Aguardando pagamento";
    }

    Color corDeFundoDoStatus(PedidoAbstract pedido) {
        if (pedido.estaCancelado()) {
            return new Color(245, 245, 245);
        }
        if (pedido.isPagamentoRegistrado()) {
            return new Color(224, 245, 224);
        }
        return new Color(255, 244, 196);
    }

    boolean possuiCreditoGerado(PedidoAbstract pedido) {
        return pedido.getCreditoGerado() > 0;
    }

    String textoDoCredito(PedidoAbstract pedido, String valorFormatado) {
        if (possuiCreditoGerado(pedido)) {
            return "Crédito gerado ao cliente: " + valorFormatado;
        }
        return "Sem crédito gerado";
    }
}
