package br.projeto.exemplo;

import br.projeto.exemplo.seeder.PedidoSeeder;
import br.projeto.exemplo.listener.ConfirmacaoPagamentoListener;
import br.projeto.exemplo.listener.CriarNovoPedidoListener;
import br.projeto.exemplo.listener.VisualizacaoPedidoListener;
import br.projeto.exemplo.repositorio.PedidoEmMemoriaRepository;
import br.projeto.painelpedidos.aplicacao.PainelPedidoFacade;
import br.projeto.painelpedidos.configuracao.ConfiguracaoPainelPedido;
import br.projeto.painelpedidos.configuracao.LeitorConfiguracaoPainelPedido;
import br.projeto.painelpedidos.model.PedidoAbstract;
import br.projeto.painelpedidos.repositorio.IPedidoRepository;
import br.projeto.painelpedidos.som.SomTomStrategy;
import br.projeto.painelpedidos.tempo.RelogioServiceSistema;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Projeto da atividade: monta o componente sobre uma origem de dados e uma
 * configuracao, e o executa. Demonstra a integracao minima esperada de qualquer
 * sistema de delivery - a instancia unica e responsabilidade do componente, que
 * apenas sinaliza a recusa.
 *
 * @author clayton
 */
public final class AplicacaoPainelPedido {

    public static void main(String[] argumentos) {
        configurarLookAndFeel();

        ConfiguracaoPainelPedido configuracao = new LeitorConfiguracaoPainelPedido(
                ".", "painel_pedidos.env").ler();

        LocalDateTime referencia = LocalDateTime.now();
        List<PedidoAbstract> pedidos = new PedidoSeeder(referencia).gerar();
        IPedidoRepository repositorio = new PedidoEmMemoriaRepository(pedidos);

        PainelPedidoFacade painelPedido = new PainelPedidoFacade(
                repositorio, configuracao, new SomTomStrategy(880, 200), new RelogioServiceSistema());
        painelPedido.setVisualizarPedidoListener(new VisualizacaoPedidoListener());
        painelPedido.setConfirmarPagamentoListener(new ConfirmacaoPagamentoListener());
        painelPedido.setCriarNovoPedidoListener(new CriarNovoPedidoListener(repositorio));

        if (!painelPedido.iniciar()) {
            System.out.println("O Painel de Acompanhamento de Pedidos ja esta aberto.");
        }
    }

    private static void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException excecao) {
            throw new IllegalStateException(
                    "Nao foi possivel configurar o Look and Feel do sistema; "
                    + "verifique o ambiente grafico e tente novamente", excecao);
        }
    }
}
