package br.projeto.painelpedidos.aplicacao;

import br.projeto.painelpedidos.configuracao.ConfiguracaoPainelPedido;
import br.projeto.painelpedidos.filtro.FabricaFiltroPeriodo;
import br.projeto.painelpedidos.filtro.IFabricaFiltroPeriodo;
import br.projeto.painelpedidos.ingestao.observer.NovoPedidoObserver;
import br.projeto.painelpedidos.ingestao.observer.PainelPedidoNovoPedidoObserver;
import br.projeto.painelpedidos.listener.IConfirmarPagamentoListener;
import br.projeto.painelpedidos.listener.ICriarNovoPedidoListener;
import br.projeto.painelpedidos.listener.IVisualizarPedidoListener;
import br.projeto.painelpedidos.model.PedidoAbstract;
import br.projeto.painelpedidos.notificacao.AlertaSonoroHandler;
import br.projeto.painelpedidos.notificacao.AtualizarPainelHandler;
import br.projeto.painelpedidos.notificacao.DestaquePedidoHandler;
import br.projeto.painelpedidos.notificacao.IServicoNotificacaoNovoPedido;
import br.projeto.painelpedidos.notificacao.MensagemUmPedidoHandler;
import br.projeto.painelpedidos.notificacao.MensagemMultiploPedidoHandler;
import br.projeto.painelpedidos.notificacao.NotificacaoNovoPedidoService;
import br.projeto.painelpedidos.notificacao.RegraHandler;
import br.projeto.painelpedidos.notificacao.SalvarPedidoHandler;
import br.projeto.painelpedidos.notificacao.SelecionarFocarHandler;
import br.projeto.painelpedidos.ordenacao.IOrdenacaoPedido;
import br.projeto.painelpedidos.ordenacao.OrdenacaoPedidoPorPrioridadeETempoNoEstado;
import br.projeto.painelpedidos.presenter.PainelPedidoPresenter;
import br.projeto.painelpedidos.repositorio.IPedidoRepository;
import br.projeto.painelpedidos.sla.AgendadorMonitorSlaService;
import br.projeto.painelpedidos.sla.IMonitorSlaService;
import br.projeto.painelpedidos.sla.MonitorSlaService;
import br.projeto.painelpedidos.sla.RegraSlaEstadoHandler;
import br.projeto.painelpedidos.sla.RegraSlaHandler;
import br.projeto.painelpedidos.sla.observer.PainelPedidoPrioridadeObserver;
import br.projeto.painelpedidos.som.IEstrategiaSom;
import br.projeto.painelpedidos.som.SomSilenciosoStrategy;
import br.projeto.painelpedidos.tempo.FonteDataPedido;
import br.projeto.painelpedidos.tempo.IFonteDataPedido;
import br.projeto.painelpedidos.tempo.IRelogioService;
import br.projeto.painelpedidos.view.PainelPedidoPanel;
import br.projeto.painelpedidos.view.PainelPedidoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Ponto de entrada do componente reutilizavel. O sistema consumidor instancia
 * esta fachada informando um {@link IPedidoRepository} (sua origem de dados) e
 * uma {@link ConfiguracaoPainelPedido}, alem dos dois listeners obrigatorios; a
 * {@link IEstrategiaSom} e o {@link IRelogioService} sao opcionais.
 *
 * <p>Nao ha simulacao, geracao de pedidos nem dados embutidos. Os novos pedidos
 * entram sempre por <b>empurrao</b> do sistema consumidor, via
 * {@link #receberNovosPedidos(List)}: o consumidor e o sujeito (a fonte do
 * evento) e o componente e o observador, que salva, atualiza o painel e
 * notifica.</p>
 *
 * <p>O componente NAO define Look and Feel: essa e uma decisao da aplicacao
 * cliente, que deve configura-lo antes de iniciar o painel.</p>
 *
 * <p>O componente <b>possui</b> a instancia unica: {@link #iniciar()} adquire a
 * trava antes de criar a janela e, nao conseguindo, devolve <code>false</code>
 * sem abrir nada; {@link #encerrar()} libera a trava. A trava e a fonte unica da
 * verdade (nao ha flag "ja aberto" em memoria), entao fechar e reabrir no mesmo
 * processo volta a abrir. O consumidor nao configura trava, caminho nem
 * singleton.</p>
 *
 * @author clayton
 */
public final class PainelPedidoFacade {

    private final IPedidoRepository pedidoRepository;
    private final ConfiguracaoPainelPedido configuracao;
    private final PainelPedidoPanel painelPedido;
    private final PainelPedidoPresenter painelPedidoPresenter;
    private final IServicoNotificacaoNovoPedido servicoNotificacao;
    private final NovoPedidoObserver novoPedidoObserver;
    private final IMonitorSlaService monitorSlaService;
    private final IRelogioService relogioService;

    private IVisualizarPedidoListener visualizarPedidoListener;
    private IConfirmarPagamentoListener confirmarPagamentoListener;
    private ICriarNovoPedidoListener criarNovoPedidoListener;
    private PainelPedidoView janela;
    private AgendadorMonitorSlaService agendadorMonitorSla;
    private TravaInstanciaUnicaArquivo travaInstanciaUnica;

    public PainelPedidoFacade(
            IPedidoRepository pedidoRepository,
            ConfiguracaoPainelPedido configuracao,
            IEstrategiaSom estrategiaSom,
            IRelogioService relogioService) {

        this.pedidoRepository = Objects.requireNonNull(
                pedidoRepository, "Informe um repositorio de pedidos valido");
        this.configuracao = Objects.requireNonNull(
                configuracao, "Informe uma configuracao do painel valida");
        Objects.requireNonNull(estrategiaSom, "Informe uma estrategia de som valida");
        this.relogioService = Objects.requireNonNull(
                relogioService, "Informe um relogio de datas valido");

        this.painelPedido = new PainelPedidoPanel();
        IFabricaFiltroPeriodo fabricaFiltroPeriodo = new FabricaFiltroPeriodo();
        IOrdenacaoPedido ordenacaoPedidos = new OrdenacaoPedidoPorPrioridadeETempoNoEstado();
        IFonteDataPedido fonteDataPedido = new FonteDataPedido(relogioService);
        IEstrategiaSom som = configuracao.isSomAtivo() ? estrategiaSom : new SomSilenciosoStrategy();

        this.painelPedidoPresenter = new PainelPedidoPresenter(
                painelPedido, pedidoRepository, fonteDataPedido, fabricaFiltroPeriodo,
                som, ordenacaoPedidos, configuracao.getDuracaoNotificacaoSegundos());

        this.servicoNotificacao = new NotificacaoNovoPedidoService(criarRegrasNotificacao());
        this.novoPedidoObserver = new PainelPedidoNovoPedidoObserver(painelPedidoPresenter, servicoNotificacao);
        this.monitorSlaService = new MonitorSlaService(criarRegrasSla(configuracao));
        this.monitorSlaService.adicionarObserver(new PainelPedidoPrioridadeObserver(painelPedidoPresenter));
    }

    public void setVisualizarPedidoListener(IVisualizarPedidoListener visualizarPedidoListener) {
        this.visualizarPedidoListener = Objects.requireNonNull(
                visualizarPedidoListener, "Informe um IVisualizarPedidoListener valido");
        this.painelPedidoPresenter.setVisualizarPedidoListener(visualizarPedidoListener);
    }

    public void setConfirmarPagamentoListener(IConfirmarPagamentoListener confirmarPagamentoListener) {
        this.confirmarPagamentoListener = Objects.requireNonNull(
                confirmarPagamentoListener, "Informe um IConfirmarPagamentoListener valido");
        this.painelPedidoPresenter.setConfirmarPagamentoListener(confirmarPagamentoListener);
    }

    public void setCriarNovoPedidoListener(ICriarNovoPedidoListener criarNovoPedidoListener) {
        this.criarNovoPedidoListener = Objects.requireNonNull(
                criarNovoPedidoListener, "Informe um ICriarNovoPedidoListener valido");
        this.painelPedidoPresenter.setCriarNovoPedidoListener(criarNovoPedidoListener);
    }

    public boolean iniciar() {
        validarAcoesObrigatorias();
        if (!adquirirTravaInstanciaUnica()) {
            return false;
        }

        janela = new PainelPedidoView(painelPedido, configuracao.getTituloJanela());
        painelPedido.getBtnFechar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                encerrar();
            }
        });
        painelPedidoPresenter.iniciar();
        janela.setVisible(true);
        iniciarMonitorSla();
        return true;
    }

    /**
     * Caminho de embutir: monta o painel e o entrega ao consumidor, que o
     * coloca no proprio conteiner. Nao cria janela; aplica a instancia unica e
     * inicia o monitor de SLA.
     */
    public boolean iniciarEmbutido() {
        validarAcoesObrigatorias();
        if (!adquirirTravaInstanciaUnica()) {
            return false;
        }
        iniciarMonitorSla();
        return true;
    }

    public PainelPedidoPanel getPainelPedido() {
        return painelPedido;
    }

    private boolean adquirirTravaInstanciaUnica() {
        TravaInstanciaUnicaArquivo trava = new TravaInstanciaUnicaArquivo(caminhoTravaPadrao());
        if (!trava.adquirir()) {
            return false;
        }
        this.travaInstanciaUnica = trava;
        return true;
    }

    public void receberNovosPedidos(List<PedidoAbstract> pedidos) {
        novoPedidoObserver.receberNovosPedidos(pedidos);
    }

    public void encerrar() {
        if (agendadorMonitorSla != null) {
            agendadorMonitorSla.parar();
        }
        if (janela != null) {
            janela.dispose();
        }
        if (travaInstanciaUnica != null) {
            travaInstanciaUnica.liberar();
            this.travaInstanciaUnica = null;
        }
    }

    private Path caminhoTravaPadrao() {
        return Paths.get(System.getProperty("java.io.tmpdir"), nomeArquivoTrava());
    }

    private String nomeArquivoTrava() {
        String identidade = configuracao.getIdentificadorAplicacao();
        StringBuilder nome = new StringBuilder("painel_pedidos-");
        for (char caractere : identidade.toCharArray()) {
            nome.append(caractereSeguro(caractere) ? caractere : '-');
        }
        return nome.append(".lock").toString();
    }

    private boolean caractereSeguro(char caractere) {
        boolean letra = (caractere >= 'a' && caractere <= 'z')
                || (caractere >= 'A' && caractere <= 'Z');
        boolean digito = caractere >= '0' && caractere <= '9';
        boolean separador = caractere == '.' || caractere == '_' || caractere == '-';
        return letra || digito || separador;
    }

    private void iniciarMonitorSla() {
        agendadorMonitorSla = new AgendadorMonitorSlaService(
                pedidoRepository, monitorSlaService, relogioService,
                (int) configuracao.getSlaMonitorIntervalo().toMillis());
        agendadorMonitorSla.iniciar();
    }

    private void validarAcoesObrigatorias() {
        if (visualizarPedidoListener == null) {
            throw new IllegalStateException(
                    "Configure IVisualizarPedidoListener na aplicacao cliente antes de iniciar o painel");
        }
        if (confirmarPagamentoListener == null) {
            throw new IllegalStateException(
                    "Configure IConfirmarPagamentoListener na aplicacao cliente antes de iniciar o painel");
        }
        if (criarNovoPedidoListener == null) {
            throw new IllegalStateException(
                    "Configure ICriarNovoPedidoListener na aplicacao cliente antes de iniciar o painel");
        }
    }

    private List<RegraHandler> criarRegrasNotificacao() {
        List<RegraHandler> regras = new ArrayList<>();
        regras.add(new SalvarPedidoHandler());
        regras.add(new AtualizarPainelHandler());
        regras.add(new SelecionarFocarHandler());
        regras.add(new AlertaSonoroHandler());
        regras.add(new MensagemUmPedidoHandler());
        regras.add(new MensagemMultiploPedidoHandler());
        regras.add(new DestaquePedidoHandler());
        return regras;
    }

    private List<RegraSlaHandler> criarRegrasSla(ConfiguracaoPainelPedido configuracao) {
        List<RegraSlaHandler> regras = new ArrayList<>();
        regras.add(new RegraSlaEstadoHandler(configuracao.getSlaPorSituacao()));
        return regras;
    }
}
