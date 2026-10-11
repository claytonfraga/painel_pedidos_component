package br.projeto.painelpedidos.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Painel de Acompanhamento de Pedidos como componente Swing embedavel.
 * Concentra apenas a construcao e os getters dos elementos visuais.
 *
 * @author clayton
 */
public class PainelPedidoPanel extends JPanel implements IPainelPedidoView {

    private JPanel pnlFiltros;
    private JLabel lblBuscarPedidoCliente;
    private JTextField txtBuscaPedidoCliente;
    private JLabel lblPeriodo;
    private JComboBox<String> cmbPeriodo;
    private JLabel lblData;
    private JSpinner spnData;
    private JButton btnAtualizarPainel;
    private JLabel lblNotificacao;
    private JPanel pnlAcompanhamento;
    private JPanel pnlColunas;
    private JPanel pnlColunaNovo;
    private JLabel lblTituloNovo;
    private JScrollPane scrNovo;
    private JPanel pnlPedidosNovo;
    private JPanel pnlColunaEmPreparo;
    private JLabel lblTituloEmPreparo;
    private JScrollPane scrEmPreparo;
    private JPanel pnlPedidosEmPreparo;
    private JPanel pnlColunaProntoParaEntrega;
    private JLabel lblTituloProntoParaEntrega;
    private JScrollPane scrProntoParaEntrega;
    private JPanel pnlPedidosProntoParaEntrega;
    private JPanel pnlColunaEmRotaDeEntrega;
    private JLabel lblTituloEmRotaDeEntrega;
    private JScrollPane scrEmRotaDeEntrega;
    private JPanel pnlPedidosEmRotaDeEntrega;
    private JPanel pnlColunaEntregue;
    private JLabel lblTituloEntregue;
    private JScrollPane scrEntregue;
    private JPanel pnlPedidosEntregue;
    private JPanel pnlColunaCancelado;
    private JLabel lblTituloCancelado;
    private JScrollPane scrCancelado;
    private JPanel pnlPedidosCancelado;
    private JPanel pnlPedidoSelecionado;
    private JLabel lblPedidoSelecionado;
    private JPanel pnlBotoesRodape;
    private JButton btnNovoPedido;
    private JButton btnVisualizarPedido;
    private JButton btnExecutarAcao;
    private JButton btnFechar;

    public PainelPedidoPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(8, 8));

        pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        pnlFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));

        lblBuscarPedidoCliente = new JLabel("Buscar pedido ou cliente:");
        pnlFiltros.add(lblBuscarPedidoCliente);

        txtBuscaPedidoCliente = new JTextField();
        txtBuscaPedidoCliente.setColumns(34);
        pnlFiltros.add(txtBuscaPedidoCliente);

        lblPeriodo = new JLabel("Período:");
        pnlFiltros.add(lblPeriodo);

        cmbPeriodo = new JComboBox<>();
        cmbPeriodo.setPreferredSize(new Dimension(230, 28));
        pnlFiltros.add(cmbPeriodo);

        lblData = new JLabel("Data:");
        lblData.setVisible(false);
        pnlFiltros.add(lblData);

        spnData = new JSpinner(new javax.swing.SpinnerDateModel());
        spnData.setEditor(new JSpinner.DateEditor(spnData, "dd/MM/yyyy"));
        spnData.setPreferredSize(new Dimension(120, 28));
        spnData.setVisible(false);
        pnlFiltros.add(spnData);

        btnAtualizarPainel = new JButton("Atualizar painel");
        btnAtualizarPainel.setPreferredSize(new Dimension(150, 30));
        pnlFiltros.add(btnAtualizarPainel);

        lblNotificacao = new JLabel();
        lblNotificacao.setPreferredSize(new Dimension(260, 28));
        lblNotificacao.setFont(lblNotificacao.getFont().deriveFont(Font.BOLD));
        lblNotificacao.setForeground(Color.WHITE);
        lblNotificacao.setHorizontalAlignment(SwingConstants.CENTER);
        lblNotificacao.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        pnlFiltros.add(lblNotificacao);

        add(pnlFiltros, BorderLayout.NORTH);

        pnlAcompanhamento = new JPanel(new BorderLayout(6, 6));
        pnlAcompanhamento.setBorder(BorderFactory.createTitledBorder("Acompanhamento de Pedidos"));

        pnlColunas = new JPanel(new GridLayout(1, 6, 8, 0));

        pnlColunaNovo = new JPanel(new BorderLayout());
        lblTituloNovo = new JLabel("Novo (0)");
        lblTituloNovo.setOpaque(true);
        lblTituloNovo.setBackground(new Color(232, 239, 247));
        lblTituloNovo.setBorder(BorderFactory.createEmptyBorder(9, 10, 9, 10));
        lblTituloNovo.setFont(lblTituloNovo.getFont().deriveFont(Font.BOLD));
        lblTituloNovo.setHorizontalAlignment(SwingConstants.LEFT);
        pnlColunaNovo.add(lblTituloNovo, BorderLayout.NORTH);
        pnlPedidosNovo = criarPainelCartoes();
        scrNovo = criarScroll(pnlPedidosNovo);
        pnlColunaNovo.add(scrNovo, BorderLayout.CENTER);
        pnlColunas.add(pnlColunaNovo);

        pnlColunaEmPreparo = new JPanel(new BorderLayout());
        lblTituloEmPreparo = new JLabel("Em preparo (0)");
        lblTituloEmPreparo.setOpaque(true);
        lblTituloEmPreparo.setBackground(new Color(218, 236, 252));
        lblTituloEmPreparo.setBorder(BorderFactory.createEmptyBorder(9, 10, 9, 10));
        lblTituloEmPreparo.setFont(lblTituloEmPreparo.getFont().deriveFont(Font.BOLD));
        lblTituloEmPreparo.setHorizontalAlignment(SwingConstants.LEFT);
        pnlColunaEmPreparo.add(lblTituloEmPreparo, BorderLayout.NORTH);
        pnlPedidosEmPreparo = criarPainelCartoes();
        scrEmPreparo = criarScroll(pnlPedidosEmPreparo);
        pnlColunaEmPreparo.add(scrEmPreparo, BorderLayout.CENTER);
        pnlColunas.add(pnlColunaEmPreparo);

        pnlColunaProntoParaEntrega = new JPanel(new BorderLayout());
        lblTituloProntoParaEntrega = new JLabel("Pronto pra entrega (0)");
        lblTituloProntoParaEntrega.setOpaque(true);
        lblTituloProntoParaEntrega.setBackground(new Color(255, 242, 196));
        lblTituloProntoParaEntrega.setBorder(BorderFactory.createEmptyBorder(9, 10, 9, 10));
        lblTituloProntoParaEntrega.setFont(lblTituloProntoParaEntrega.getFont().deriveFont(Font.BOLD));
        lblTituloProntoParaEntrega.setHorizontalAlignment(SwingConstants.LEFT);
        pnlColunaProntoParaEntrega.add(lblTituloProntoParaEntrega, BorderLayout.NORTH);
        pnlPedidosProntoParaEntrega = criarPainelCartoes();
        scrProntoParaEntrega = criarScroll(pnlPedidosProntoParaEntrega);
        pnlColunaProntoParaEntrega.add(scrProntoParaEntrega, BorderLayout.CENTER);
        pnlColunas.add(pnlColunaProntoParaEntrega);

        pnlColunaEmRotaDeEntrega = new JPanel(new BorderLayout());
        lblTituloEmRotaDeEntrega = new JLabel("Em rota de entrega (0)");
        lblTituloEmRotaDeEntrega.setOpaque(true);
        lblTituloEmRotaDeEntrega.setBackground(new Color(252, 228, 209));
        lblTituloEmRotaDeEntrega.setBorder(BorderFactory.createEmptyBorder(9, 10, 9, 10));
        lblTituloEmRotaDeEntrega.setFont(lblTituloEmRotaDeEntrega.getFont().deriveFont(Font.BOLD));
        lblTituloEmRotaDeEntrega.setHorizontalAlignment(SwingConstants.LEFT);
        pnlColunaEmRotaDeEntrega.add(lblTituloEmRotaDeEntrega, BorderLayout.NORTH);
        pnlPedidosEmRotaDeEntrega = criarPainelCartoes();
        scrEmRotaDeEntrega = criarScroll(pnlPedidosEmRotaDeEntrega);
        pnlColunaEmRotaDeEntrega.add(scrEmRotaDeEntrega, BorderLayout.CENTER);
        pnlColunas.add(pnlColunaEmRotaDeEntrega);

        pnlColunaEntregue = new JPanel(new BorderLayout());
        lblTituloEntregue = new JLabel("Entregue (0)");
        lblTituloEntregue.setOpaque(true);
        lblTituloEntregue.setBackground(new Color(222, 242, 222));
        lblTituloEntregue.setBorder(BorderFactory.createEmptyBorder(9, 10, 9, 10));
        lblTituloEntregue.setFont(lblTituloEntregue.getFont().deriveFont(Font.BOLD));
        lblTituloEntregue.setHorizontalAlignment(SwingConstants.LEFT);
        pnlColunaEntregue.add(lblTituloEntregue, BorderLayout.NORTH);
        pnlPedidosEntregue = criarPainelCartoes();
        scrEntregue = criarScroll(pnlPedidosEntregue);
        pnlColunaEntregue.add(scrEntregue, BorderLayout.CENTER);
        pnlColunas.add(pnlColunaEntregue);

        pnlColunaCancelado = new JPanel(new BorderLayout());
        lblTituloCancelado = new JLabel("Cancelado (0)");
        lblTituloCancelado.setOpaque(true);
        lblTituloCancelado.setBackground(new Color(249, 221, 221));
        lblTituloCancelado.setBorder(BorderFactory.createEmptyBorder(9, 10, 9, 10));
        lblTituloCancelado.setFont(lblTituloCancelado.getFont().deriveFont(Font.BOLD));
        lblTituloCancelado.setHorizontalAlignment(SwingConstants.LEFT);
        pnlColunaCancelado.add(lblTituloCancelado, BorderLayout.NORTH);
        pnlPedidosCancelado = criarPainelCartoes();
        scrCancelado = criarScroll(pnlPedidosCancelado);
        pnlColunaCancelado.add(scrCancelado, BorderLayout.CENTER);
        pnlColunas.add(pnlColunaCancelado);

        pnlAcompanhamento.add(pnlColunas, BorderLayout.CENTER);
        add(pnlAcompanhamento, BorderLayout.CENTER);

        pnlPedidoSelecionado = new JPanel(new BorderLayout(8, 0));
        pnlPedidoSelecionado.setBorder(BorderFactory.createTitledBorder("Pedido selecionado"));

        lblPedidoSelecionado = new JLabel("Selecione um pedido para visualizar detalhes e executar ações.");
        lblPedidoSelecionado.setForeground(new Color(90, 90, 90));
        pnlPedidoSelecionado.add(lblPedidoSelecionado, BorderLayout.CENTER);

        pnlBotoesRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 5));

        btnNovoPedido = new JButton("Novo Pedido");
        btnNovoPedido.setPreferredSize(new Dimension(150, 32));
        pnlBotoesRodape.add(btnNovoPedido);

        btnVisualizarPedido = new JButton("Visualizar pedido");
        btnVisualizarPedido.setPreferredSize(new Dimension(170, 32));
        pnlBotoesRodape.add(btnVisualizarPedido);

        btnExecutarAcao = new JButton("Executar ação");
        btnExecutarAcao.setPreferredSize(new Dimension(170, 32));
        pnlBotoesRodape.add(btnExecutarAcao);

        btnFechar = new JButton("Fechar");
        btnFechar.setPreferredSize(new Dimension(140, 32));
        pnlBotoesRodape.add(btnFechar);

        pnlPedidoSelecionado.add(pnlBotoesRodape, BorderLayout.EAST);
        add(pnlPedidoSelecionado, BorderLayout.SOUTH);
    }

    private JPanel criarPainelCartoes() {
        JPanel pnlCartoes = new JPanel();
        pnlCartoes.setLayout(new BoxLayout(pnlCartoes, BoxLayout.Y_AXIS));
        pnlCartoes.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        pnlCartoes.setBackground(new Color(248, 250, 252));
        return pnlCartoes;
    }

    private JScrollPane criarScroll(JPanel pnlCartoes) {
        JScrollPane scr = new JScrollPane();
        scr.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scr.getVerticalScrollBar().setUnitIncrement(16);
        scr.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 235)));
        scr.setViewportView(pnlCartoes);
        return scr;
    }

    @Override
    public JPanel getPainelPrincipal() {
        return this;
    }

    @Override
    public void maximizar() {
        Frame frame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, this);
        if (frame != null) {
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        }
    }

    @Override
    public JButton getBtnAtualizarPainel() {
        return btnAtualizarPainel;
    }

    @Override
    public JButton getBtnExecutarAcao() {
        return btnExecutarAcao;
    }

    @Override
    public JButton getBtnFechar() {
        return btnFechar;
    }

    @Override
    public JButton getBtnVisualizarPedido() {
        return btnVisualizarPedido;
    }

    @Override
    public JButton getBtnNovoPedido() {
        return btnNovoPedido;
    }

    @Override
    public JTextField getTxtBuscaPedidoCliente() {
        return txtBuscaPedidoCliente;
    }

    @Override
    public JLabel getLblNotificacao() {
        return lblNotificacao;
    }

    @Override
    public JLabel getLblData() {
        return lblData;
    }

    @Override
    public JSpinner getSpnData() {
        return spnData;
    }

    @Override
    public JComboBox<String> getCmbPeriodo() {
        return cmbPeriodo;
    }

    @Override
    public JPanel getPnlPedidosNovo() {
        return pnlPedidosNovo;
    }

    @Override
    public JPanel getPnlPedidosEmPreparo() {
        return pnlPedidosEmPreparo;
    }

    @Override
    public JPanel getPnlPedidosProntoParaEntrega() {
        return pnlPedidosProntoParaEntrega;
    }

    @Override
    public JPanel getPnlPedidosEmRotaDeEntrega() {
        return pnlPedidosEmRotaDeEntrega;
    }

    @Override
    public JPanel getPnlPedidosEntregue() {
        return pnlPedidosEntregue;
    }

    @Override
    public JPanel getPnlPedidosCancelado() {
        return pnlPedidosCancelado;
    }

    @Override
    public JLabel getLblPedidoSelecionado() {
        return lblPedidoSelecionado;
    }

    @Override
    public JLabel getLblTituloNovo() {
        return lblTituloNovo;
    }

    @Override
    public JLabel getLblTituloEmPreparo() {
        return lblTituloEmPreparo;
    }

    @Override
    public JLabel getLblTituloProntoParaEntrega() {
        return lblTituloProntoParaEntrega;
    }

    @Override
    public JLabel getLblTituloEmRotaDeEntrega() {
        return lblTituloEmRotaDeEntrega;
    }

    @Override
    public JLabel getLblTituloEntregue() {
        return lblTituloEntregue;
    }

    @Override
    public JLabel getLblTituloCancelado() {
        return lblTituloCancelado;
    }
}
