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
public class PainelPedidoView extends JPanel implements IPainelPedidoView {

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    // End of variables declaration//GEN-END:variables

    public PainelPedidoView() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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
        pnlPedidosNovo = new JPanel();
        pnlPedidosNovo.setLayout(new BoxLayout(pnlPedidosNovo, BoxLayout.Y_AXIS));
        pnlPedidosNovo.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        pnlPedidosNovo.setBackground(new Color(248, 250, 252));
        scrNovo = new JScrollPane();
        scrNovo.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrNovo.getVerticalScrollBar().setUnitIncrement(16);
        scrNovo.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 235)));
        scrNovo.setViewportView(pnlPedidosNovo);
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
        pnlPedidosEmPreparo = new JPanel();
        pnlPedidosEmPreparo.setLayout(new BoxLayout(pnlPedidosEmPreparo, BoxLayout.Y_AXIS));
        pnlPedidosEmPreparo.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        pnlPedidosEmPreparo.setBackground(new Color(248, 250, 252));
        scrEmPreparo = new JScrollPane();
        scrEmPreparo.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrEmPreparo.getVerticalScrollBar().setUnitIncrement(16);
        scrEmPreparo.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 235)));
        scrEmPreparo.setViewportView(pnlPedidosEmPreparo);
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
        pnlPedidosProntoParaEntrega = new JPanel();
        pnlPedidosProntoParaEntrega.setLayout(new BoxLayout(pnlPedidosProntoParaEntrega, BoxLayout.Y_AXIS));
        pnlPedidosProntoParaEntrega.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        pnlPedidosProntoParaEntrega.setBackground(new Color(248, 250, 252));
        scrProntoParaEntrega = new JScrollPane();
        scrProntoParaEntrega.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrProntoParaEntrega.getVerticalScrollBar().setUnitIncrement(16);
        scrProntoParaEntrega.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 235)));
        scrProntoParaEntrega.setViewportView(pnlPedidosProntoParaEntrega);
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
        pnlPedidosEmRotaDeEntrega = new JPanel();
        pnlPedidosEmRotaDeEntrega.setLayout(new BoxLayout(pnlPedidosEmRotaDeEntrega, BoxLayout.Y_AXIS));
        pnlPedidosEmRotaDeEntrega.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        pnlPedidosEmRotaDeEntrega.setBackground(new Color(248, 250, 252));
        scrEmRotaDeEntrega = new JScrollPane();
        scrEmRotaDeEntrega.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrEmRotaDeEntrega.getVerticalScrollBar().setUnitIncrement(16);
        scrEmRotaDeEntrega.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 235)));
        scrEmRotaDeEntrega.setViewportView(pnlPedidosEmRotaDeEntrega);
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
        pnlPedidosEntregue = new JPanel();
        pnlPedidosEntregue.setLayout(new BoxLayout(pnlPedidosEntregue, BoxLayout.Y_AXIS));
        pnlPedidosEntregue.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        pnlPedidosEntregue.setBackground(new Color(248, 250, 252));
        scrEntregue = new JScrollPane();
        scrEntregue.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrEntregue.getVerticalScrollBar().setUnitIncrement(16);
        scrEntregue.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 235)));
        scrEntregue.setViewportView(pnlPedidosEntregue);
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
        pnlPedidosCancelado = new JPanel();
        pnlPedidosCancelado.setLayout(new BoxLayout(pnlPedidosCancelado, BoxLayout.Y_AXIS));
        pnlPedidosCancelado.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        pnlPedidosCancelado.setBackground(new Color(248, 250, 252));
        scrCancelado = new JScrollPane();
        scrCancelado.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrCancelado.getVerticalScrollBar().setUnitIncrement(16);
        scrCancelado.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 235)));
        scrCancelado.setViewportView(pnlPedidosCancelado);
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
    }// </editor-fold>//GEN-END:initComponents



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
