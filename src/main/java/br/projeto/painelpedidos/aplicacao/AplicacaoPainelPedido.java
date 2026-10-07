package br.projeto.painelpedidos.aplicacao;

import br.projeto.painelpedidos.configuracao.LeitorEnv;
import br.projeto.painelpedidos.dados.CatalogoPedido;
import br.projeto.painelpedidos.filtro.FabricaFiltroPeriodo;
import br.projeto.painelpedidos.filtro.IFabricaFiltroPeriodo;
import br.projeto.painelpedidos.model.Pedido;
import br.projeto.painelpedidos.notificacao.AlertaSonoroHandler;
import br.projeto.painelpedidos.notificacao.AtualizarPainelHandler;
import br.projeto.painelpedidos.notificacao.DestaquePedidosHandler;
import br.projeto.painelpedidos.notificacao.IServicoNotificacaoNovoPedido;
import br.projeto.painelpedidos.notificacao.MensagemUmPedidoHandler;
import br.projeto.painelpedidos.notificacao.MensagemVariosPedidosHandler;
import br.projeto.painelpedidos.notificacao.NotificacaoNovoPedidoService;
import br.projeto.painelpedidos.notificacao.RegraHandler;
import br.projeto.painelpedidos.notificacao.SalvarPedidosHandler;
import br.projeto.painelpedidos.notificacao.SelecionarFocarHandler;
import br.projeto.painelpedidos.ordenacao.IOrdenacaoPedidos;
import br.projeto.painelpedidos.ordenacao.OrdenacaoPedidoPorPrioridadeEPosicao;
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
import br.projeto.painelpedidos.simulacao.observer.NovoPedidoObserver;
import br.projeto.painelpedidos.simulacao.observer.PainelPedidoNovoPedidoObserver;
import br.projeto.painelpedidos.sla.AgendadorMonitorSla;
import br.projeto.painelpedidos.sla.IMonitorSlaService;
import br.projeto.painelpedidos.sla.MonitorSlaService;
import br.projeto.painelpedidos.sla.RegraSlaHandler;
import br.projeto.painelpedidos.sla.RegraSlaEmPreparoHandler;
import br.projeto.painelpedidos.sla.RegraSlaEmRotaDeEntregaHandler;
import br.projeto.painelpedidos.sla.RegraSlaNovoHandler;
import br.projeto.painelpedidos.sla.RegraSlaProntoParaEntregaHandler;
import br.projeto.painelpedidos.sla.observer.PainelPedidoPrioridadeObserver;
import br.projeto.painelpedidos.som.IEstrategiaSom;
import br.projeto.painelpedidos.som.SomTomStrategy;
import br.projeto.painelpedidos.tempo.ITempoEntregaService;
import br.projeto.painelpedidos.tempo.TempoEntregaService;
import br.projeto.painelpedidos.view.IPainelPedidoView;
import br.projeto.painelpedidos.view.PainelPedidoView;
import java.nio.file.Paths;
import java.time.Duration;
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
        int slaNovoMinutos = leitorEnv.lerInteiro("SLA_NOVO_MINUTOS", 5);
        int slaEmPreparoMinutos = leitorEnv.lerInteiro("SLA_EM_PREPARO_MINUTOS", 20);
        int slaEntregaBairroMinutos = leitorEnv.lerInteiro("SLA_ENTREGA_BAIRRO_MINUTOS", 10);
        int slaMonitorIntervalo = leitorEnv.lerInteiro("SLA_MONITOR_INTERVALO_SEGUNDOS", 30);

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
        IOrdenacaoPedidos ordenacaoPedidos = new OrdenacaoPedidoPorPrioridadeEPosicao();

        IServicoNotificacaoNovoPedido servicoNotificacao =
                new NotificacaoNovoPedidoService(criarRegrasNotificacao());
        PainelPedidoPresenter painelPedidoPresenter = new PainelPedidoPresenter(
                painelPedidoView, pedidoRepository, fonteDataPedido,
                fabricaFiltroPeriodo, estrategiaSom, ordenacaoPedidos, duracaoNotificacao);

        NovoPedidoObserver novoPedidoObserver = new PainelPedidoNovoPedidoObserver(
                painelPedidoPresenter, servicoNotificacao);
        simuladorNovoPedido.iniciar(novoPedidoObserver);

        ITempoEntregaService tempoEntregaService = new TempoEntregaService(
                Duration.ofMinutes(slaEntregaBairroMinutos));
        IMonitorSlaService monitorSlaService = new MonitorSlaService(
                criarRegrasSla(slaNovoMinutos, slaEmPreparoMinutos));
        monitorSlaService.adicionarObserver(
                new PainelPedidoPrioridadeObserver(painelPedidoPresenter));

        AgendadorMonitorSla agendadorMonitorSla = new AgendadorMonitorSla(
                pedidoRepository, monitorSlaService, tempoEntregaService,
                slaMonitorIntervalo * 1000);
        agendadorMonitorSla.iniciar();
    }

    private List<RegraHandler> criarRegrasNotificacao() {
        List<RegraHandler> regras = new ArrayList<>();
        regras.add(new SalvarPedidosHandler());
        regras.add(new AtualizarPainelHandler());
        regras.add(new SelecionarFocarHandler());
        regras.add(new AlertaSonoroHandler());
        regras.add(new MensagemUmPedidoHandler());
        regras.add(new MensagemVariosPedidosHandler());
        regras.add(new DestaquePedidosHandler());
        return regras;
    }

    private List<RegraSlaHandler> criarRegrasSla(
            int slaNovoMinutos, int slaEmPreparoMinutos) {

        List<RegraSlaHandler> regras = new ArrayList<>();
        regras.add(new RegraSlaNovoHandler(Duration.ofMinutes(slaNovoMinutos)));
        regras.add(new RegraSlaEmPreparoHandler(Duration.ofMinutes(slaEmPreparoMinutos)));
        regras.add(new RegraSlaProntoParaEntregaHandler());
        regras.add(new RegraSlaEmRotaDeEntregaHandler());
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
