package br.projeto.painelpedidos.state;

import br.projeto.painelpedidos.model.Pedido;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public abstract class PedidoState {

    protected final Pedido pedido;

    public PedidoState(Pedido pedido) {
        this.pedido = Objects.requireNonNull(pedido, "pedido");
    }

    public abstract String getNome();

    public boolean ehNovo() {
        return false;
    }

    public boolean ehEmPreparo() {
        return false;
    }

    public boolean ehProntoParaEntrega() {
        return false;
    }

    public boolean ehEmRotaDeEntrega() {
        return false;
    }

    public boolean ehEntregue() {
        return false;
    }

    public boolean ehCancelado() {
        return false;
    }

    public void registrarPagamento(String formaPagamento) {
        throw new RuntimeException("Nao e possivel registrar pagamento de um pedido " + getNome());
    }

    public void marcarProntoParaEntrega() {
        throw new RuntimeException("Nao e possivel marcar como pronto para entrega um pedido " + getNome());
    }

    public void sairParaEntrega() {
        throw new RuntimeException("Nao e possivel sair para entrega um pedido " + getNome());
    }

    public void marcarEntregue() {
        throw new RuntimeException("Nao e possivel marcar como entregue um pedido " + getNome());
    }

    public void cancelar() {
        throw new RuntimeException("Nao e possivel cancelar um pedido " + getNome());
    }
}
