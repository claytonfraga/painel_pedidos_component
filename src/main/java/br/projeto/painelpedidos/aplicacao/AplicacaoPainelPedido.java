package br.projeto.painelpedidos.aplicacao;

import br.projeto.painelpedidos.configuracao.LeitorEnv;
import br.projeto.painelpedidos.dados.CatalogoPedido;
import br.projeto.painelpedidos.filtro.FabricaFiltroPeriodo;
import br.projeto.painelpedidos.filtro.IFabricaFiltroPeriodo;
import br.projeto.painelpedidos.model.Pedido;
import br.projeto.painelpedidos.notificacao.AlertaSonoroChainOfResponsibility;
import br.projeto.painelpedidos.notificacao.AtualizarPainelChainOfResponsibility;
import br.projeto.painelpedidos.notificacao.DestaquePedidosChainOfResponsibility;
import br.projeto.painelpedidos.notificacao.IServicoNotificacaoNovoPedido;
import br.projeto.painelpedidos.notificacao.MensagemUmPedidoChainOfResponsibility;
import br.projeto.painelpedidos.notificacao.MensagemVariosPedidosChainOfResponsibility;
import br.projeto.painelpedidos.notificacao.NotificacaoNovoPedidoService;
import br.projeto.painelpedidos.notificacao.RegraChainOfResponsibility;
import br.projeto.painelpedidos.notificacao.SalvarPedidosChainOfResponsibility;
import br.projeto.painelpedidos.notificacao.SelecionarFocarChainOfResponsibility;
import br.projeto.painelpedidos.presenter.PainelPedidoPresenter;
import br.projeto.painelpedidos.repositorio.IPedidoRepository;
import br.projeto.painelpedidos.repositorio.PedidoEmMemoriaRepository;
import br.projeto.painelpedidos.seeder.PedidoSeeder;
import br.projeto.painelpedidos.simulacao.AleatoriedadeServiceRandom;
import br.projeto.painelpedidos.simulacao.ConfiguracaoSimulacao;
import br.projeto.painelpedidos.simulacao.FonteDataPedido;
import br.projeto.painelpedidos.simulacao.GeradorPedidoSimulado;
import br.projeto.painelpedidos.simulacao.IAleatoriedadeService;
import br.projeto.painelpedidos.simulacao.IFonteDataPedido;
import br.projeto.painelpedidos.simulacao.IGeradorPedido;
import br.projeto.painelpedidos.simulacao.IRelogioService;
import br.projeto.painelpedidos.simulacao.ISimuladorNovoPedido;
import br.projeto.painelpedidos.simulacao.RelogioServiceSistema;
import br.projeto.painelpedidos.simulacao.SimuladorNovoPedido;
import br.projeto.painelpedidos.som.IEstrategiaSom;
import br.projeto.painelpedidos.som.SomTomStrategy;
import br.projeto.painelpedidos.view.IPainelPedidoView;
import br.projeto.painelpedidos.view.PainelPedidoView;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;

/**
 *
 * @author clayton
 */
public class AplicacaoPainelPedido implements IExecucaoAplicacao {

    public static void main(String[] argumentos) {
        IExecucaoAplicacao aplicacao = new AplicacaoPainelPedido();
        ITravaInstanciaUnica trava = new TravaInstanciaUnicaArquivo(
                Paths.get(System.getProperty("java.io.tmpdir"), "painelpedido.lock"));

        boolean iniciada = InstanciaUnicaAplicacao.getInstance().iniciar(aplicacao, trava);
        if (!iniciada) {
            System.out.println("O Painel de Acompanhamento de Pedidos ja esta aberto.");
        }
    }

    @Override
    public void executar() {
        configurarLookAndFeel();

        LeitorEnv leitorEnv = new LeitorEnv();
        int quantidadeSeed = leitorEnv.lerInteiro("SEED_QUANTIDADE", 1000);
        int intervaloMinimo = leitorEnv.lerInteiro("SIMULACAO_INTERVALO_MINIMO_SEGUNDOS", 30);
        int intervaloMaximo = leitorEnv.lerInteiro("SIMULACAO_INTERVALO_MAXIMO_SEGUNDOS", 90);
        int duracaoNotificacao = leitorEnv.lerInteiro("SIMULACAO_MENSAGEM_SEGUNDOS", 10);
        int pedidosPorCiclo = leitorEnv.lerInteiro("SIMULACAO_PEDIDOS_POR_CICLO", 4);

        CatalogoPedido catalogoPedido = new CatalogoPedido();
        LocalDateTime agora = LocalDateTime.now();

        List<Pedido> pedidosIniciais = new PedidoSeeder(quantidadeSeed, agora, catalogoPedido).gerar();
        IPainelPedidoView painelPedidoView = new PainelPedidoView();
        IPedidoRepository pedidoRepository = new PedidoEmMemoriaRepository(pedidosIniciais);

        ConfiguracaoSimulacao configuracao = new ConfiguracaoSimulacao(
                intervaloMinimo, intervaloMaximo, duracaoNotificacao, pedidosPorCiclo);
        IAleatoriedadeService aleatoriedadeService = new AleatoriedadeServiceRandom();
        IRelogioService relogioService = new RelogioServiceSistema();
        IFonteDataPedido fonteDataPedido = new FonteDataPedido(relogioService);
        IGeradorPedido geradorPedido = new GeradorPedidoSimulado(
                pedidoRepository, fonteDataPedido, aleatoriedadeService, catalogoPedido);
        ISimuladorNovoPedido simuladorNovoPedido = new SimuladorNovoPedido(
                configuracao, geradorPedido, aleatoriedadeService);
        IFabricaFiltroPeriodo fabricaFiltroPeriodo = new FabricaFiltroPeriodo();
        IEstrategiaSom estrategiaSom = new SomTomStrategy(880, 200);
        IServicoNotificacaoNovoPedido servicoNotificacao =
                new NotificacaoNovoPedidoService(criarRegrasNotificacao());

        new PainelPedidoPresenter(
                painelPedidoView, pedidoRepository, simuladorNovoPedido,
                fonteDataPedido, fabricaFiltroPeriodo, servicoNotificacao,
                estrategiaSom, duracaoNotificacao);
    }

    private List<RegraChainOfResponsibility> criarRegrasNotificacao() {
        List<RegraChainOfResponsibility> regras = new ArrayList<>();
        regras.add(new SalvarPedidosChainOfResponsibility());
        regras.add(new AtualizarPainelChainOfResponsibility());
        regras.add(new SelecionarFocarChainOfResponsibility());
        regras.add(new AlertaSonoroChainOfResponsibility());
        regras.add(new MensagemUmPedidoChainOfResponsibility());
        regras.add(new MensagemVariosPedidosChainOfResponsibility());
        regras.add(new DestaquePedidosChainOfResponsibility());
        return regras;
    }

    private void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException excecao) {
            throw new IllegalStateException("Nao foi possivel configurar o Look and Feel do sistema", excecao);
        }
    }
}
