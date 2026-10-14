package br.projeto.painelpedidos.internal.notificacao;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.util.ArrayList;
import java.util.List;

/**
 * Fake de {@link IAcaoNotificacaoNovoPedido} para os testes da cadeia: registra
 * cada acionamento, para assercao sem depender de Swing.
 *
 * @author clayton
 */
public final class AcoesNotificacaoFake implements IAcaoNotificacaoNovoPedido {

    private final List<PedidoAbstract> salvos = new ArrayList<>();
    private final List<PedidoAbstract> destacados = new ArrayList<>();
    private final List<String> mensagens = new ArrayList<>();
    private int atualizacoes;
    private PedidoAbstract focado;

    @Override
    public void salvarPedidos(List<PedidoAbstract> pedidos) {
        salvos.addAll(pedidos);
    }

    @Override
    public void atualizarPainel() {
        atualizacoes++;
    }

    @Override
    public void selecionarFocar(PedidoAbstract pedido) {
        this.focado = pedido;
    }

    @Override
    public void exibirMensagem(String mensagem) {
        mensagens.add(mensagem);
    }

    @Override
    public void destacarPedidos(List<PedidoAbstract> pedidos) {
        destacados.addAll(pedidos);
    }

    public List<PedidoAbstract> getSalvos() {
        return new ArrayList<>(salvos);
    }

    public List<PedidoAbstract> getDestacados() {
        return new ArrayList<>(destacados);
    }

    public List<String> getMensagens() {
        return new ArrayList<>(mensagens);
    }

    public int getAtualizacoes() {
        return atualizacoes;
    }

    public PedidoAbstract getFocado() {
        return focado;
    }
}
