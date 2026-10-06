package br.projeto.painelpedidos.presenter;

import br.projeto.painelpedidos.filtro.IFabricaFiltroPeriodo;
import br.projeto.painelpedidos.filtro.IFiltroPeriodo;
import br.projeto.painelpedidos.model.Pedido;
import br.projeto.painelpedidos.notificacao.IAcoesNotificacaoNovoPedido;
import br.projeto.painelpedidos.ordenacao.IOrdenacaoPedidos;
import br.projeto.painelpedidos.repositorio.IPedidoRepository;
import br.projeto.painelpedidos.simulacao.IFonteDataPedido;
import br.projeto.painelpedidos.sla.observer.IPainelPedidoAtualizador;
import br.projeto.painelpedidos.som.IEstrategiaSom;
import br.projeto.painelpedidos.view.IPainelPedidoView;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 *
 * @author clayton
 */
public class PainelPedidoPresenter implements IAcoesNotificacaoNovoPedido, IPainelPedidoAtualizador {

    private final IPainelPedidoView painelPedidoView;
    private final IPedidoRepository pedidoRepository;
    private final Map<String, Pedido> pedidosPorIdentificador;
    private final Map<String, JPanel> cartoesPorIdentificador;
    private final NumberFormat formatadorMoeda;
    private final DateTimeFormatter formatadorHora;
    private final DateTimeFormatter formatadorDataHora;
    private final IFonteDataPedido fonteDataPedido;
    private final IFabricaFiltroPeriodo fabricaFiltroPeriodo;
    private final IEstrategiaSom estrategiaSom;
    private final IOrdenacaoPedidos ordenacaoPedidos;
    private final int duracaoNotificacaoSegundos;

    private Timer timerNotificacao;
    private Timer timerDestaque;
    private List<JPanel> pnlCartoesDestacados = new ArrayList<>();
    private boolean destaqueAceso;
    private int ticksRestantesDestaque;
    private String identificadorPedidoSelecionado;

    public PainelPedidoPresenter(
            IPainelPedidoView painelPedidoView,
            IPedidoRepository pedidoRepository,
            IFonteDataPedido fonteDataPedido,
            IFabricaFiltroPeriodo fabricaFiltroPeriodo,
            IEstrategiaSom estrategiaSom,
            IOrdenacaoPedidos ordenacaoPedidos,
            int duracaoNotificacaoSegundos) {

        this.painelPedidoView = Objects.requireNonNull(painelPedidoView, "painelPedidoView");
        this.pedidoRepository = Objects.requireNonNull(pedidoRepository, "pedidoRepository");
        this.fonteDataPedido = Objects.requireNonNull(fonteDataPedido, "fonteDataPedido");
        this.fabricaFiltroPeriodo = Objects.requireNonNull(fabricaFiltroPeriodo, "fabricaFiltroPeriodo");
        this.estrategiaSom = Objects.requireNonNull(estrategiaSom, "estrategiaSom");
        this.ordenacaoPedidos = Objects.requireNonNull(ordenacaoPedidos, "ordenacaoPedidos");
        this.duracaoNotificacaoSegundos = duracaoNotificacaoSegundos;
        this.pedidosPorIdentificador = new LinkedHashMap<>();
        this.cartoesPorIdentificador = new LinkedHashMap<>();
        this.formatadorMoeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
        this.formatadorHora = DateTimeFormatter.ofPattern("HH:mm");
        this.formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM HH:mm");

        configurarPeriodos();
        configurarListeners();
        configurarEstadoInicialDaView();
        atualizarVisibilidadeData();
        painelPedidoView.getJanelaPrincipal().setLocationRelativeTo(null);
        atualizarPainel();
        painelPedidoView.getJanelaPrincipal().setVisible(true);
    }

    @Override
    public void atualizarPedidos(List<Pedido> pedidos) {
        atualizarPainel();
    }

    @Override
    public void salvarPedidos(List<Pedido> pedidos) {
        for (Pedido pedido : pedidos) {
            pedidoRepository.salvar(pedido);
        }
    }

    @Override
    public void selecionarFocar(Pedido pedido) {
        selecionarPedido(pedido.getIdentificador());
        focarCartaoPedido(pedido.getIdentificador());
    }

    @Override
    public void emitirAlertaSonoro() {
        estrategiaSom.tocar();
    }

    @Override
    public void exibirMensagem(String mensagem) {
        JLabel lblNotificacao = painelPedidoView.getLblNotificacao();
        lblNotificacao.setText(mensagem);
        lblNotificacao.setOpaque(true);
        lblNotificacao.setBackground(new Color(194, 88, 0));
        lblNotificacao.setForeground(Color.WHITE);
        if (timerNotificacao != null) {
            timerNotificacao.stop();
        }
        timerNotificacao = new Timer(duracaoNotificacaoSegundos * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                lblNotificacao.setText("");
                lblNotificacao.setOpaque(false);
            }
        });
        timerNotificacao.setRepeats(false);
        timerNotificacao.start();
    }

    @Override
    public void destacarPedidos(List<Pedido> pedidos) {
        pararTimerDestaque();
        pnlCartoesDestacados = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            JPanel pnlCartao = obterPainelCartao(pedido.getIdentificador());
            if (pnlCartao != null) {
                pnlCartoesDestacados.add(pnlCartao);
            }
        }
        if (pnlCartoesDestacados.isEmpty()) {
            return;
        }
        destaqueAceso = true;
        aplicarDestaque(destaqueAceso);
        ticksRestantesDestaque = duracaoNotificacaoSegundos * 2;
        timerDestaque = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                destaqueAceso = !destaqueAceso;
                aplicarDestaque(destaqueAceso);
                ticksRestantesDestaque--;
                if (ticksRestantesDestaque <= 0) {
                    pararTimerDestaque();
                    aplicarDestaque(false);
                    validarSelecaoAposAtualizacao();
                }
            }
        });
        timerDestaque.start();
    }

    private void aplicarDestaque(boolean aceso) {
        for (JPanel pnlCartao : pnlCartoesDestacados) {
            if (aceso) {
                pnlCartao.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(194, 88, 0), 2),
                        BorderFactory.createEmptyBorder(7, 7, 7, 7)));
                pnlCartao.setBackground(new Color(255, 224, 178));
            } else {
                pnlCartao.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(176, 181, 189)),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)));
                pnlCartao.setBackground(Color.WHITE);
            }
        }
    }

    private void pararTimerDestaque() {
        if (timerDestaque != null) {
            timerDestaque.stop();
        }
    }

    private JPanel obterPainelCartao(String identificador) {
        JPanel pnlConjunto = cartoesPorIdentificador.get(identificador);
        if (pnlConjunto == null || pnlConjunto.getComponentCount() == 0) {
            return null;
        }
        return (JPanel) pnlConjunto.getComponent(0);
    }

    private void focarCartaoPedido(String identificador) {
        JPanel pnlCartao = obterPainelCartao(identificador);
        if (pnlCartao == null) {
            return;
        }
        pnlCartao.requestFocusInWindow();
        pnlCartao.scrollRectToVisible(pnlCartao.getBounds());
    }

    private void configurarPeriodos() {
        painelPedidoView.getCmbPeriodo().removeAllItems();
        painelPedidoView.getCmbPeriodo().addItem(
                "Hoje (" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")");
        painelPedidoView.getCmbPeriodo().addItem("Últimos 7 dias");
        painelPedidoView.getCmbPeriodo().addItem("Últimos 30 dias");
        painelPedidoView.getCmbPeriodo().addItem("Todos");
        painelPedidoView.getCmbPeriodo().addItem("Data específica");
        painelPedidoView.getCmbPeriodo().setSelectedIndex(0);
    }

    private void configurarListeners() {
        painelPedidoView.getBtnAtualizarPainel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                atualizarPainel();
            }
        });
        painelPedidoView.getTxtBuscaPedidoCliente().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                atualizarPainel();
            }
        });
        painelPedidoView.getBtnVisualizarPedido().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                visualizarPedidoSelecionado();
            }
        });
        painelPedidoView.getBtnExecutarAcao().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                executarAcaoSelecionada();
            }
        });
        painelPedidoView.getBtnFechar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                fecharPainel();
            }
        });
        painelPedidoView.getCmbPeriodo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                atualizarVisibilidadeData();
            }
        });
    }

    private void configurarEstadoInicialDaView() {
        painelPedidoView.getBtnVisualizarPedido().setEnabled(false);
        painelPedidoView.getBtnExecutarAcao().setVisible(false);
        painelPedidoView.getBtnExecutarAcao().setEnabled(false);
    }

    public final void atualizarPainel() {
        try {
            List<Pedido> pedidosFiltrados = filtrarPedidos(pedidoRepository.buscarTodos());

            limparColunas();
            pedidosPorIdentificador.clear();
            cartoesPorIdentificador.clear();

            List<Pedido> pedidosNovo = new ArrayList<>();
            List<Pedido> pedidosEmPreparo = new ArrayList<>();
            List<Pedido> pedidosProntoParaEntrega = new ArrayList<>();
            List<Pedido> pedidosEmRotaDeEntrega = new ArrayList<>();
            List<Pedido> pedidosEntregue = new ArrayList<>();
            List<Pedido> pedidosCancelado = new ArrayList<>();

            for (Pedido pedido : pedidosFiltrados) {
                pedidosPorIdentificador.put(pedido.getIdentificador(), pedido);
                if (pedido.estaNovo()) {
                    pedidosNovo.add(pedido);
                } else if (pedido.estaEmPreparo()) {
                    pedidosEmPreparo.add(pedido);
                } else if (pedido.estaProntoParaEntrega()) {
                    pedidosProntoParaEntrega.add(pedido);
                } else if (pedido.estaEmRotaDeEntrega()) {
                    pedidosEmRotaDeEntrega.add(pedido);
                } else if (pedido.estaEntregue()) {
                    pedidosEntregue.add(pedido);
                } else if (pedido.estaCancelado()) {
                    pedidosCancelado.add(pedido);
                }
            }

            adicionarCartoes(painelPedidoView.getPnlPedidosNovo(), pedidosNovo);
            adicionarCartoes(painelPedidoView.getPnlPedidosEmPreparo(), pedidosEmPreparo);
            adicionarCartoes(painelPedidoView.getPnlPedidosProntoParaEntrega(), pedidosProntoParaEntrega);
            adicionarCartoes(painelPedidoView.getPnlPedidosEmRotaDeEntrega(), pedidosEmRotaDeEntrega);
            adicionarCartoes(painelPedidoView.getPnlPedidosEntregue(), pedidosEntregue);
            adicionarCartoes(painelPedidoView.getPnlPedidosCancelado(), pedidosCancelado);

            atualizarTitulosColunas(
                    pedidosNovo.size(),
                    pedidosEmPreparo.size(),
                    pedidosProntoParaEntrega.size(),
                    pedidosEmRotaDeEntrega.size(),
                    pedidosEntregue.size(),
                    pedidosCancelado.size());

            validarSelecaoAposAtualizacao();
            revalidarColunas();
        } catch (RuntimeException excecao) {
            JOptionPane.showMessageDialog(
                    painelPedidoView.getJanelaPrincipal(),
                    excecao.getMessage(),
                    "Erro ao atualizar pedidos",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void adicionarCartoes(JPanel pnlDestino, List<Pedido> pedidos) {
        pedidos.sort(ordenacaoPedidos);
        for (Pedido pedido : pedidos) {
            JPanel pnlCartaoPedido = criarCartaoPedido(pedido);
            cartoesPorIdentificador.put(pedido.getIdentificador(), pnlCartaoPedido);
            pnlDestino.add(pnlCartaoPedido);
        }
    }

    private List<Pedido> filtrarPedidos(List<Pedido> pedidosConsultados) {
        String textoBusca = painelPedidoView.getTxtBuscaPedidoCliente().getText();
        String buscaNormalizada = textoBusca == null ? "" : textoBusca.trim().toLowerCase(Locale.ROOT);
        LocalDate hoje = LocalDate.now();

        IFiltroPeriodo filtroPeriodo = filtroPeriodoSelecionado();
        atualizarFonteDataPedido(filtroPeriodo);

        List<Pedido> pedidosFiltrados = new ArrayList<>();
        for (Pedido pedido : pedidosConsultados) {
            if (!correspondeBusca(pedido, buscaNormalizada)) {
                continue;
            }
            if (!filtroPeriodo.aceita(pedido.getDataHora(), hoje)) {
                continue;
            }
            pedidosFiltrados.add(pedido);
        }

        return pedidosFiltrados;
    }

    private IFiltroPeriodo filtroPeriodoSelecionado() {
        int indicePeriodo = painelPedidoView.getCmbPeriodo().getSelectedIndex();
        LocalDate dataEspecifica = null;
        if (fabricaFiltroPeriodo.exigeData(indicePeriodo)) {
            dataEspecifica = lerDataSelecionada();
        }
        return fabricaFiltroPeriodo.criar(indicePeriodo, dataEspecifica);
    }

    private void atualizarFonteDataPedido(IFiltroPeriodo filtroPeriodo) {
        LocalDate dataNovoPedido = filtroPeriodo.getDataParaNovoPedido();
        if (dataNovoPedido == null) {
            fonteDataPedido.usarDataAtual();
            return;
        }
        fonteDataPedido.usarData(dataNovoPedido);
    }

    private LocalDate lerDataSelecionada() {
        Date valor = (Date) painelPedidoView.getSpnData().getValue();
        return valor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void atualizarVisibilidadeData() {
        boolean exigeData = fabricaFiltroPeriodo.exigeData(
                painelPedidoView.getCmbPeriodo().getSelectedIndex());
        painelPedidoView.getLblData().setVisible(exigeData);
        painelPedidoView.getSpnData().setVisible(exigeData);
    }

    private boolean correspondeBusca(Pedido pedido, String buscaNormalizada) {
        if (buscaNormalizada.isEmpty()) {
            return true;
        }

        String identificadorNormalizado = pedido.getIdentificador().toLowerCase(Locale.ROOT);
        String clienteNormalizado = pedido.getNomeCliente().toLowerCase(Locale.ROOT);
        return identificadorNormalizado.contains(buscaNormalizada)
                || clienteNormalizado.contains(buscaNormalizada);
    }

    private JPanel criarCartaoPedido(Pedido pedido) {
        JPanel pnlCartaoPedido = new JPanel(new GridBagLayout());
        pnlCartaoPedido.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlCartaoPedido.setBackground(Color.WHITE);
        pnlCartaoPedido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(176, 181, 189)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        pnlCartaoPedido.setMaximumSize(new Dimension(Integer.MAX_VALUE, calcularAlturaCartao(pedido)));
        pnlCartaoPedido.setFocusable(true);
        pnlCartaoPedido.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evento) {
                selecionarPedido(pedido.getIdentificador());
            }
        });
        pnlCartaoPedido.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evento) {
                selecionarPedido(pedido.getIdentificador());
            }
        });

        GridBagConstraints restricoes = new GridBagConstraints();
        restricoes.gridx = 0;
        restricoes.gridy = 0;
        restricoes.weightx = 1.0;
        restricoes.fill = GridBagConstraints.HORIZONTAL;
        restricoes.anchor = GridBagConstraints.WEST;
        restricoes.insets = new Insets(0, 0, 3, 0);

        JPanel pnlCabecalho = new JPanel(new java.awt.BorderLayout());
        pnlCabecalho.setOpaque(false);

        JButton btnSelecionarPedido = new JButton(formatarIdentificador(pedido.getIdentificador()));
        btnSelecionarPedido.setBorderPainted(false);
        btnSelecionarPedido.setContentAreaFilled(false);
        btnSelecionarPedido.setFocusPainted(false);
        btnSelecionarPedido.setHorizontalAlignment(SwingConstants.LEFT);
        btnSelecionarPedido.setFont(btnSelecionarPedido.getFont().deriveFont(Font.BOLD, 14f));
        btnSelecionarPedido.setMargin(new Insets(0, 0, 0, 0));
        btnSelecionarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                selecionarPedido(pedido.getIdentificador());
            }
        });

        JLabel lblHorario = new JLabel(formatarHorario(pedido.getDataHora()));
        pnlCabecalho.add(btnSelecionarPedido, java.awt.BorderLayout.CENTER);
        pnlCabecalho.add(lblHorario, java.awt.BorderLayout.EAST);
        pnlCartaoPedido.add(pnlCabecalho, restricoes);

        restricoes.gridy++;
        pnlCartaoPedido.add(criarRotulo(pedido.getNomeCliente(), false), restricoes);

        restricoes.gridy++;
        JLabel lblEndereco = criarRotulo(pedido.getEnderecoEntrega(), false);
        lblEndereco.setForeground(new Color(65, 65, 65));
        pnlCartaoPedido.add(lblEndereco, restricoes);

        restricoes.gridy++;
        JPanel pnlValorPagamento = new JPanel(new java.awt.BorderLayout());
        pnlValorPagamento.setOpaque(false);
        JLabel lblValor = criarRotulo(formatarValorMonetario(pedido.getValorTotal()), true);
        JLabel lblPagamento = criarRotulo(formatarFormaPagamento(pedido), false);
        pnlValorPagamento.add(lblValor, java.awt.BorderLayout.WEST);
        pnlValorPagamento.add(lblPagamento, java.awt.BorderLayout.EAST);
        pnlCartaoPedido.add(pnlValorPagamento, restricoes);

        restricoes.gridy++;
        pnlCartaoPedido.add(criarRotuloStatus(pedido), restricoes);

        if (pedido.isPrioridade()) {
            restricoes.gridy++;
            pnlCartaoPedido.add(criarRotuloPrioridade(), restricoes);
        }

        if (pedido.estaCancelado()) {
            restricoes.gridy++;
            pnlCartaoPedido.add(criarRotuloCredito(pedido), restricoes);
        }

        JButton btnAcaoPrincipal = criarBotaoAcaoPrincipal(pedido);
        if (btnAcaoPrincipal != null) {
            restricoes.gridy++;
            restricoes.insets = new Insets(4, 0, 3, 0);
            pnlCartaoPedido.add(btnAcaoPrincipal, restricoes);
        }

        if (!pedido.estaCancelado()) {
            restricoes.gridy++;
            restricoes.insets = new Insets(0, 0, 0, 0);
            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evento) {
                    selecionarPedido(pedido.getIdentificador());
                    cancelarPedidoSelecionado();
                }
            });
            pnlCartaoPedido.add(btnCancelar, restricoes);
        }

        JPanel pnlEspacador = new JPanel();
        pnlEspacador.setOpaque(false);
        pnlEspacador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 7));

        JPanel pnlConjunto = new JPanel();
        pnlConjunto.setLayout(new javax.swing.BoxLayout(pnlConjunto, javax.swing.BoxLayout.Y_AXIS));
        pnlConjunto.setOpaque(false);
        pnlConjunto.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlConjunto.add(pnlCartaoPedido);
        pnlConjunto.add(pnlEspacador);

        return pnlConjunto;
    }

    private JLabel criarRotulo(String texto, boolean negrito) {
        JLabel lbl = new JLabel(texto == null ? "" : texto);
        if (negrito) {
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        }
        return lbl;
    }

    private JLabel criarRotuloStatus(Pedido pedido) {
        JLabel lblStatus;

        if (pedido.estaCancelado()) {
            lblStatus = criarRotulo("Pedido cancelado", false);
            lblStatus.setBackground(new Color(245, 245, 245));
        } else if (pedido.isPagamentoRegistrado()) {
            lblStatus = criarRotulo("Pagamento confirmado", false);
            lblStatus.setBackground(new Color(224, 245, 224));
        } else {
            lblStatus = criarRotulo("Aguardando pagamento", false);
            lblStatus.setBackground(new Color(255, 244, 196));
        }

        lblStatus.setOpaque(true);
        lblStatus.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        return lblStatus;
    }

    private JLabel criarRotuloCredito(Pedido pedido) {
        double creditoGerado = pedido.getCreditoGerado();
        boolean possuiCredito = creditoGerado > 0;
        String textoCredito = possuiCredito
                ? "Crédito gerado ao cliente: " + formatarValorMonetario(creditoGerado)
                : "Sem crédito gerado";

        JLabel lblCredito = criarRotulo(textoCredito, possuiCredito);
        lblCredito.setOpaque(true);
        lblCredito.setBackground(possuiCredito
                ? new Color(224, 245, 224)
                : new Color(245, 245, 245));
        lblCredito.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        return lblCredito;
    }

    private JLabel criarRotuloPrioridade() {
        JLabel lblPrioridade = new JLabel("Prioridade");
        lblPrioridade.setOpaque(true);
        lblPrioridade.setBackground(new Color(200, 0, 0));
        lblPrioridade.setForeground(Color.WHITE);
        lblPrioridade.setFont(lblPrioridade.getFont().deriveFont(Font.BOLD));
        lblPrioridade.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));
        return lblPrioridade;
    }

    private JButton criarBotaoAcaoPrincipal(Pedido pedido) {
        JButton btnAcaoPrincipal = new JButton();

        if (pedido.estaNovo()) {
            btnAcaoPrincipal.setText("Confirmar pagamento");
        } else if (pedido.estaEmPreparo()) {
            btnAcaoPrincipal.setText("Pronto pra entrega");
        } else if (pedido.estaProntoParaEntrega()) {
            btnAcaoPrincipal.setText("Sair pra entrega");
        } else if (pedido.estaEmRotaDeEntrega()) {
            btnAcaoPrincipal.setText("Entregue");
        } else if (pedido.estaEntregue()) {
            btnAcaoPrincipal.setText("Pedido finalizado");
            btnAcaoPrincipal.setEnabled(false);
            return btnAcaoPrincipal;
        } else if (pedido.estaCancelado()) {
            btnAcaoPrincipal.setText("Criar novo pedido");
        } else {
            return null;
        }

        btnAcaoPrincipal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                selecionarPedido(pedido.getIdentificador());
                executarAcaoPrincipal(pedido);
            }
        });
        return btnAcaoPrincipal;
    }

    private void selecionarPedido(String identificador) {
        identificadorPedidoSelecionado = identificador;
        Pedido pedidoSelecionado = pedidosPorIdentificador.get(identificador);

        restaurarBordasCartoes();
        destacarCartaoSelecionado(identificador);

        if (pedidoSelecionado == null) {
            limparSelecao();
            return;
        }

        painelPedidoView.getLblPedidoSelecionado().setText(
                formatarIdentificador(pedidoSelecionado.getIdentificador())
                + " - " + pedidoSelecionado.getNomeCliente()
                + " - " + pedidoSelecionado.getSituacao());
        painelPedidoView.getBtnVisualizarPedido().setEnabled(true);
        boolean possuiAcao = possuiAcaoPrincipalExecutavel(pedidoSelecionado);
        painelPedidoView.getBtnExecutarAcao().setText(obterTextoAcaoPrincipal(pedidoSelecionado));
        painelPedidoView.getBtnExecutarAcao().setVisible(possuiAcao);
        painelPedidoView.getBtnExecutarAcao().setEnabled(possuiAcao);
        painelPedidoView.getJanelaPrincipal().revalidate();
        painelPedidoView.getJanelaPrincipal().repaint();
    }

    private void restaurarBordasCartoes() {
        for (JPanel pnlConjuntoCartao : cartoesPorIdentificador.values()) {
            if (pnlConjuntoCartao.getComponentCount() > 0) {
                JPanel pnlCartao = (JPanel) pnlConjuntoCartao.getComponent(0);
                pnlCartao.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(176, 181, 189)),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)));
                pnlCartao.setBackground(Color.WHITE);
            }
        }
    }

    private void destacarCartaoSelecionado(String identificador) {
        JPanel pnlConjuntoSelecionado = cartoesPorIdentificador.get(identificador);
        if (pnlConjuntoSelecionado == null || pnlConjuntoSelecionado.getComponentCount() == 0) {
            return;
        }

        JPanel pnlCartao = (JPanel) pnlConjuntoSelecionado.getComponent(0);
        pnlCartao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(37, 99, 235), 2),
                BorderFactory.createEmptyBorder(7, 7, 7, 7)));
        pnlCartao.setBackground(corFundoTituloRaia(identificador));
    }

    private Color corFundoTituloRaia(String identificador) {
        Pedido pedido = pedidosPorIdentificador.get(identificador);
        if (pedido == null) {
            return Color.WHITE;
        }
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

    private void executarAcaoSelecionada() {
        Pedido pedidoSelecionado = obterPedidoSelecionado();
        if (pedidoSelecionado != null) {
            executarAcaoPrincipal(pedidoSelecionado);
        }
    }

    private void executarAcaoPrincipal(Pedido pedido) {
        try {
            if (pedido.estaNovo()) {
                registrarPagamento(pedido);
                return;
            }

            if (pedido.estaEmPreparo()) {
                pedido.marcarProntoParaEntrega();
                pedidoRepository.salvar(pedido);
                atualizarPainel();
                return;
            }

            if (pedido.estaProntoParaEntrega()) {
                pedido.sairParaEntrega();
                pedidoRepository.salvar(pedido);
                atualizarPainel();
                return;
            }

            if (pedido.estaEmRotaDeEntrega()) {
                pedido.marcarEntregue();
                pedidoRepository.salvar(pedido);
                atualizarPainel();
                return;
            }

            if (pedido.estaCancelado()) {
                criarNovoPedidoAPartirCancelado(pedido);
            }
        } catch (RuntimeException excecao) {
            JOptionPane.showMessageDialog(
                    painelPedidoView.getJanelaPrincipal(),
                    excecao.getMessage(),
                    "Não foi possível executar a ação",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void registrarPagamento(Pedido pedido) {
        Object[] formasPagamento = {
            "Pix - chave CPF",
            "Pix - chave e-mail",
            "Cartão de crédito"
        };

        Object formaPagamentoSelecionada = JOptionPane.showInputDialog(
                painelPedidoView.getJanelaPrincipal(),
                "Selecione a forma de pagamento",
                "Registrar pagamento",
                JOptionPane.PLAIN_MESSAGE,
                null,
                formasPagamento,
                formasPagamento[0]);

        if (formaPagamentoSelecionada == null) {
            return;
        }

        pedido.registrarPagamento(formaPagamentoSelecionada.toString());
        pedidoRepository.salvar(pedido);
        atualizarPainel();
    }

    private void criarNovoPedidoAPartirCancelado(Pedido pedidoCancelado) {
        atualizarFonteDataPedido(filtroPeriodoSelecionado());
        String novoIdentificador = pedidoRepository.gerarProximoIdentificador();
        Pedido novoPedido = pedidoCancelado.copiarComoNovo(
                novoIdentificador, fonteDataPedido.obterDataHora());
        pedidoRepository.salvar(novoPedido);
        identificadorPedidoSelecionado = novoIdentificador;
        atualizarPainel();
    }

    private void cancelarPedidoSelecionado() {
        Pedido pedidoSelecionado = obterPedidoSelecionado();
        if (pedidoSelecionado == null || pedidoSelecionado.estaCancelado()) {
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(
                painelPedidoView.getJanelaPrincipal(),
                "Confirma o cancelamento do pedido "
                + formatarIdentificador(pedidoSelecionado.getIdentificador()) + "?",
                "Cancelar pedido",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            pedidoSelecionado.cancelar();
            pedidoRepository.salvar(pedidoSelecionado);
            atualizarPainel();
        } catch (RuntimeException excecao) {
            JOptionPane.showMessageDialog(
                    painelPedidoView.getJanelaPrincipal(),
                    excecao.getMessage(),
                    "Não foi possível cancelar o pedido",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void visualizarPedidoSelecionado() {
        Pedido pedidoSelecionado = obterPedidoSelecionado();
        if (pedidoSelecionado == null) {
            return;
        }

        StringBuilder texto = new StringBuilder();
        texto.append("Pedido: ").append(formatarIdentificador(pedidoSelecionado.getIdentificador())).append('\n');
        texto.append("Cliente: ").append(pedidoSelecionado.getNomeCliente()).append('\n');
        texto.append("Endereço de entrega: ").append(pedidoSelecionado.getEnderecoEntrega()).append('\n');
        texto.append("Data e hora: ").append(pedidoSelecionado.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append('\n');
        texto.append("Situação: ").append(pedidoSelecionado.getSituacao()).append('\n');
        texto.append("Valor: ").append(formatarValorMonetario(pedidoSelecionado.getValorTotal())).append('\n');
        texto.append("Forma de pagamento: ")
                .append(pedidoSelecionado.isPagamentoRegistrado() ? pedidoSelecionado.getFormaPagamento() : "Não registrada");

        if (pedidoSelecionado.isPagamentoRegistrado()) {
            texto.append('\n');
            texto.append("Data e hora do pagamento: ")
                    .append(pedidoSelecionado.getDataHoraPagamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        }

        JOptionPane.showMessageDialog(
                painelPedidoView.getJanelaPrincipal(),
                texto.toString(),
                "Visualização do pedido",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void fecharPainel() {
        painelPedidoView.getJanelaPrincipal().dispose();
    }

    private Pedido obterPedidoSelecionado() {
        if (identificadorPedidoSelecionado == null) {
            return null;
        }
        return pedidosPorIdentificador.get(identificadorPedidoSelecionado);
    }

    private String obterTextoAcaoPrincipal(Pedido pedido) {
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

    private boolean possuiAcaoPrincipalExecutavel(Pedido pedido) {
        return !pedido.estaEntregue();
    }

    private void limparColunas() {
        painelPedidoView.getPnlPedidosNovo().removeAll();
        painelPedidoView.getPnlPedidosEmPreparo().removeAll();
        painelPedidoView.getPnlPedidosProntoParaEntrega().removeAll();
        painelPedidoView.getPnlPedidosEmRotaDeEntrega().removeAll();
        painelPedidoView.getPnlPedidosEntregue().removeAll();
        painelPedidoView.getPnlPedidosCancelado().removeAll();
    }

    private void atualizarTitulosColunas(
            int quantidadeNovo,
            int quantidadeEmPreparo,
            int quantidadeProntoParaEntrega,
            int quantidadeEmRotaDeEntrega,
            int quantidadeEntregue,
            int quantidadeCancelado) {

        painelPedidoView.getLblTituloNovo().setText("Novo (" + quantidadeNovo + ")");
        painelPedidoView.getLblTituloEmPreparo().setText("Em preparo (" + quantidadeEmPreparo + ")");
        painelPedidoView.getLblTituloProntoParaEntrega().setText(
                "Pronto pra entrega (" + quantidadeProntoParaEntrega + ")");
        painelPedidoView.getLblTituloEmRotaDeEntrega().setText(
                "Em rota de entrega (" + quantidadeEmRotaDeEntrega + ")");
        painelPedidoView.getLblTituloEntregue().setText("Entregue (" + quantidadeEntregue + ")");
        painelPedidoView.getLblTituloCancelado().setText("Cancelado (" + quantidadeCancelado + ")");
    }

    private void validarSelecaoAposAtualizacao() {
        if (identificadorPedidoSelecionado == null
                || !pedidosPorIdentificador.containsKey(identificadorPedidoSelecionado)) {
            limparSelecao();
            return;
        }
        selecionarPedido(identificadorPedidoSelecionado);
    }

    private void limparSelecao() {
        identificadorPedidoSelecionado = null;
        painelPedidoView.getLblPedidoSelecionado().setText(
                "Selecione um pedido para visualizar detalhes e executar acoes.");
        painelPedidoView.getBtnVisualizarPedido().setEnabled(false);
        painelPedidoView.getBtnExecutarAcao().setText("Executar ação");
        painelPedidoView.getBtnExecutarAcao().setVisible(false);
        painelPedidoView.getBtnExecutarAcao().setEnabled(false);
        painelPedidoView.getJanelaPrincipal().revalidate();
    }

    private void revalidarColunas() {
        painelPedidoView.getPnlPedidosNovo().revalidate();
        painelPedidoView.getPnlPedidosEmPreparo().revalidate();
        painelPedidoView.getPnlPedidosProntoParaEntrega().revalidate();
        painelPedidoView.getPnlPedidosEmRotaDeEntrega().revalidate();
        painelPedidoView.getPnlPedidosEntregue().revalidate();
        painelPedidoView.getPnlPedidosCancelado().revalidate();
        painelPedidoView.getJanelaPrincipal().repaint();
    }

    private String formatarIdentificador(String identificador) {
        return identificador.startsWith("#") ? identificador : "#" + identificador;
    }

    private String formatarHorario(LocalDateTime dataHora) {
        LocalDate dataPedido = dataHora.toLocalDate();
        LocalDate hoje = LocalDate.now();

        if (dataPedido.equals(hoje)) {
            return dataHora.format(formatadorHora);
        }
        if (dataPedido.equals(hoje.minusDays(1))) {
            return "Ontem " + dataHora.format(formatadorHora);
        }
        return dataHora.format(formatadorDataHora);
    }

    private String formatarFormaPagamento(Pedido pedido) {
        if (!pedido.isPagamentoRegistrado()) {
            return "";
        }
        return pedido.getFormaPagamento();
    }

    private String formatarValorMonetario(double valor) {
        return formatadorMoeda.format(valor).replace('\u00A0', ' ');
    }

    private int calcularAlturaCartao(Pedido pedido) {
        if (pedido.estaCancelado()) {
            return 220;
        }
        return 235;
    }
}