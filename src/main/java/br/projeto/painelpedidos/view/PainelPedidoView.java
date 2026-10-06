package br.projeto.painelpedidos.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author clayton
 */
public class PainelPedidoView extends javax.swing.JFrame implements IPainelPedidoView {

    public PainelPedidoView() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlFiltros = new javax.swing.JPanel();
        lblBuscarPedidoCliente = new javax.swing.JLabel();
        txtBuscaPedidoCliente = new javax.swing.JTextField();
        lblPeriodo = new javax.swing.JLabel();
        cmbPeriodo = new javax.swing.JComboBox<>();
        lblData = new javax.swing.JLabel();
        spnData = new javax.swing.JSpinner();
        btnAtualizarPainel = new javax.swing.JButton();
        lblNotificacao = new javax.swing.JLabel();
        pnlAcompanhamento = new javax.swing.JPanel();
        pnlColunas = new javax.swing.JPanel();
        pnlColunaNovo = new javax.swing.JPanel();
        lblTituloNovo = new javax.swing.JLabel();
        scrNovo = new javax.swing.JScrollPane();
        pnlPedidosNovo = new javax.swing.JPanel();
        pnlColunaEmPreparo = new javax.swing.JPanel();
        lblTituloEmPreparo = new javax.swing.JLabel();
        scrEmPreparo = new javax.swing.JScrollPane();
        pnlPedidosEmPreparo = new javax.swing.JPanel();
        pnlColunaProntoParaEntrega = new javax.swing.JPanel();
        lblTituloProntoParaEntrega = new javax.swing.JLabel();
        scrProntoParaEntrega = new javax.swing.JScrollPane();
        pnlPedidosProntoParaEntrega = new javax.swing.JPanel();
        pnlColunaEmRotaDeEntrega = new javax.swing.JPanel();
        lblTituloEmRotaDeEntrega = new javax.swing.JLabel();
        scrEmRotaDeEntrega = new javax.swing.JScrollPane();
        pnlPedidosEmRotaDeEntrega = new javax.swing.JPanel();
        pnlColunaEntregue = new javax.swing.JPanel();
        lblTituloEntregue = new javax.swing.JLabel();
        scrEntregue = new javax.swing.JScrollPane();
        pnlPedidosEntregue = new javax.swing.JPanel();
        pnlColunaCancelado = new javax.swing.JPanel();
        lblTituloCancelado = new javax.swing.JLabel();
        scrCancelado = new javax.swing.JScrollPane();
        pnlPedidosCancelado = new javax.swing.JPanel();
        pnlPedidoSelecionado = new javax.swing.JPanel();
        lblPedidoSelecionado = new javax.swing.JLabel();
        pnlBotoesRodape = new javax.swing.JPanel();
        btnVisualizarPedido = new javax.swing.JButton();
        btnExecutarAcao = new javax.swing.JButton();
        btnFechar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("POC Delivery - Painel de Acompanhamento de Pedidos");
        setMinimumSize(new Dimension(1180, 760));
        setPreferredSize(new Dimension(1380, 980));
        getContentPane().setLayout(new java.awt.BorderLayout(8, 8));

        pnlFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));
        pnlFiltros.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 10));

        lblBuscarPedidoCliente.setText("Buscar pedido ou cliente:");
        pnlFiltros.add(lblBuscarPedidoCliente);

        txtBuscaPedidoCliente.setColumns(34);
        pnlFiltros.add(txtBuscaPedidoCliente);

        lblPeriodo.setText("Período:");
        pnlFiltros.add(lblPeriodo);

        cmbPeriodo.setPreferredSize(new java.awt.Dimension(230, 28));
        pnlFiltros.add(cmbPeriodo);

        lblData.setText("Data:");
        lblData.setVisible(false);
        pnlFiltros.add(lblData);

        spnData.setModel(new javax.swing.SpinnerDateModel());
        spnData.setEditor(new javax.swing.JSpinner.DateEditor(spnData, "dd/MM/yyyy"));
        spnData.setPreferredSize(new java.awt.Dimension(120, 28));
        spnData.setVisible(false);
        pnlFiltros.add(spnData);

        btnAtualizarPainel.setText("Atualizar painel");
        btnAtualizarPainel.setPreferredSize(new java.awt.Dimension(150, 30));
        pnlFiltros.add(btnAtualizarPainel);

        lblNotificacao.setPreferredSize(new java.awt.Dimension(260, 28));
        lblNotificacao.setFont(lblNotificacao.getFont().deriveFont(Font.BOLD));
        lblNotificacao.setForeground(java.awt.Color.WHITE);
        lblNotificacao.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNotificacao.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        pnlFiltros.add(lblNotificacao);

        getContentPane().add(pnlFiltros, java.awt.BorderLayout.NORTH);

        pnlAcompanhamento.setBorder(BorderFactory.createTitledBorder("Acompanhamento de Pedidos"));
        pnlAcompanhamento.setLayout(new java.awt.BorderLayout(6, 6));

        pnlColunas.setLayout(new java.awt.GridLayout(1, 6, 8, 0));

        pnlColunaNovo.setLayout(new java.awt.BorderLayout());
        lblTituloNovo.setText("Novo (0)");
        lblTituloNovo.setOpaque(true);
        lblTituloNovo.setBackground(new Color(232, 239, 247));
        lblTituloNovo.setBorder(BorderFactory.createEmptyBorder(9, 10, 9, 10));
        lblTituloNovo.setFont(lblTituloNovo.getFont().deriveFont(Font.BOLD));
        lblTituloNovo.setHorizontalAlignment(SwingConstants.LEFT);
        pnlColunaNovo.add(lblTituloNovo, java.awt.BorderLayout.NORTH);
        scrNovo.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrNovo.getVerticalScrollBar().setUnitIncrement(16);
        scrNovo.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 235)));
        pnlPedidosNovo.setLayout(new BoxLayout(pnlPedidosNovo, BoxLayout.Y_AXIS));
        pnlPedidosNovo.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        pnlPedidosNovo.setBackground(new Color(248, 250, 252));
        scrNovo.setViewportView(pnlPedidosNovo);
        pnlColunaNovo.add(scrNovo, java.awt.BorderLayout.CENTER);
        pnlColunas.add(pnlColunaNovo);

        pnlColunaEmPreparo.setLayout(new java.awt.BorderLayout());
        lblTituloEmPreparo.setText("Em preparo (0)");
        lblTituloEmPreparo.setOpaque(true);
        lblTituloEmPreparo.setBackground(new Color(218, 236, 252));
        lblTituloEmPreparo.setBorder(BorderFactory.createEmptyBorder(9, 10, 9, 10));
        lblTituloEmPreparo.setFont(lblTituloEmPreparo.getFont().deriveFont(Font.BOLD));
        lblTituloEmPreparo.setHorizontalAlignment(SwingConstants.LEFT);
        pnlColunaEmPreparo.add(lblTituloEmPreparo, java.awt.BorderLayout.NORTH);
        scrEmPreparo.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrEmPreparo.getVerticalScrollBar().setUnitIncrement(16);
        scrEmPreparo.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 235)));
        pnlPedidosEmPreparo.setLayout(new BoxLayout(pnlPedidosEmPreparo, BoxLayout.Y_AXIS));
        pnlPedidosEmPreparo.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        pnlPedidosEmPreparo.setBackground(new Color(248, 250, 252));
        scrEmPreparo.setViewportView(pnlPedidosEmPreparo);
        pnlColunaEmPreparo.add(scrEmPreparo, java.awt.BorderLayout.CENTER);
        pnlColunas.add(pnlColunaEmPreparo);

        pnlColunaProntoParaEntrega.setLayout(new java.awt.BorderLayout());
        lblTituloProntoParaEntrega.setText("Pronto pra entrega (0)");
        lblTituloProntoParaEntrega.setOpaque(true);
        lblTituloProntoParaEntrega.setBackground(new Color(255, 242, 196));
        lblTituloProntoParaEntrega.setBorder(BorderFactory.createEmptyBorder(9, 10, 9, 10));
        lblTituloProntoParaEntrega.setFont(lblTituloProntoParaEntrega.getFont().deriveFont(Font.BOLD));
        lblTituloProntoParaEntrega.setHorizontalAlignment(SwingConstants.LEFT);
        pnlColunaProntoParaEntrega.add(lblTituloProntoParaEntrega, java.awt.BorderLayout.NORTH);
        scrProntoParaEntrega.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrProntoParaEntrega.getVerticalScrollBar().setUnitIncrement(16);
        scrProntoParaEntrega.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 235)));
        pnlPedidosProntoParaEntrega.setLayout(new BoxLayout(pnlPedidosProntoParaEntrega, BoxLayout.Y_AXIS));
        pnlPedidosProntoParaEntrega.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        pnlPedidosProntoParaEntrega.setBackground(new Color(248, 250, 252));
        scrProntoParaEntrega.setViewportView(pnlPedidosProntoParaEntrega);
        pnlColunaProntoParaEntrega.add(scrProntoParaEntrega, java.awt.BorderLayout.CENTER);
        pnlColunas.add(pnlColunaProntoParaEntrega);

        pnlColunaEmRotaDeEntrega.setLayout(new java.awt.BorderLayout());
        lblTituloEmRotaDeEntrega.setText("Em rota de entrega (0)");
        lblTituloEmRotaDeEntrega.setOpaque(true);
        lblTituloEmRotaDeEntrega.setBackground(new Color(252, 228, 209));
        lblTituloEmRotaDeEntrega.setBorder(BorderFactory.createEmptyBorder(9, 10, 9, 10));
        lblTituloEmRotaDeEntrega.setFont(lblTituloEmRotaDeEntrega.getFont().deriveFont(Font.BOLD));
        lblTituloEmRotaDeEntrega.setHorizontalAlignment(SwingConstants.LEFT);
        pnlColunaEmRotaDeEntrega.add(lblTituloEmRotaDeEntrega, java.awt.BorderLayout.NORTH);
        scrEmRotaDeEntrega.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrEmRotaDeEntrega.getVerticalScrollBar().setUnitIncrement(16);
        scrEmRotaDeEntrega.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 235)));
        pnlPedidosEmRotaDeEntrega.setLayout(new BoxLayout(pnlPedidosEmRotaDeEntrega, BoxLayout.Y_AXIS));
        pnlPedidosEmRotaDeEntrega.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        pnlPedidosEmRotaDeEntrega.setBackground(new Color(248, 250, 252));
        scrEmRotaDeEntrega.setViewportView(pnlPedidosEmRotaDeEntrega);
        pnlColunaEmRotaDeEntrega.add(scrEmRotaDeEntrega, java.awt.BorderLayout.CENTER);
        pnlColunas.add(pnlColunaEmRotaDeEntrega);

        pnlColunaEntregue.setLayout(new java.awt.BorderLayout());
        lblTituloEntregue.setText("Entregue (0)");
        lblTituloEntregue.setOpaque(true);
        lblTituloEntregue.setBackground(new Color(222, 242, 222));
        lblTituloEntregue.setBorder(BorderFactory.createEmptyBorder(9, 10, 9, 10));
        lblTituloEntregue.setFont(lblTituloEntregue.getFont().deriveFont(Font.BOLD));
        lblTituloEntregue.setHorizontalAlignment(SwingConstants.LEFT);
        pnlColunaEntregue.add(lblTituloEntregue, java.awt.BorderLayout.NORTH);
        scrEntregue.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrEntregue.getVerticalScrollBar().setUnitIncrement(16);
        scrEntregue.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 235)));
        pnlPedidosEntregue.setLayout(new BoxLayout(pnlPedidosEntregue, BoxLayout.Y_AXIS));
        pnlPedidosEntregue.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        pnlPedidosEntregue.setBackground(new Color(248, 250, 252));
        scrEntregue.setViewportView(pnlPedidosEntregue);
        pnlColunaEntregue.add(scrEntregue, java.awt.BorderLayout.CENTER);
        pnlColunas.add(pnlColunaEntregue);

        pnlColunaCancelado.setLayout(new java.awt.BorderLayout());
        lblTituloCancelado.setText("Cancelado (0)");
        lblTituloCancelado.setOpaque(true);
        lblTituloCancelado.setBackground(new Color(249, 221, 221));
        lblTituloCancelado.setBorder(BorderFactory.createEmptyBorder(9, 10, 9, 10));
        lblTituloCancelado.setFont(lblTituloCancelado.getFont().deriveFont(Font.BOLD));
        lblTituloCancelado.setHorizontalAlignment(SwingConstants.LEFT);
        pnlColunaCancelado.add(lblTituloCancelado, java.awt.BorderLayout.NORTH);
        scrCancelado.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrCancelado.getVerticalScrollBar().setUnitIncrement(16);
        scrCancelado.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 235)));
        pnlPedidosCancelado.setLayout(new BoxLayout(pnlPedidosCancelado, BoxLayout.Y_AXIS));
        pnlPedidosCancelado.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        pnlPedidosCancelado.setBackground(new Color(248, 250, 252));
        scrCancelado.setViewportView(pnlPedidosCancelado);
        pnlColunaCancelado.add(scrCancelado, java.awt.BorderLayout.CENTER);
        pnlColunas.add(pnlColunaCancelado);

        pnlAcompanhamento.add(pnlColunas, java.awt.BorderLayout.CENTER);
        getContentPane().add(pnlAcompanhamento, java.awt.BorderLayout.CENTER);

        pnlPedidoSelecionado.setBorder(BorderFactory.createTitledBorder("Pedido selecionado"));
        pnlPedidoSelecionado.setLayout(new java.awt.BorderLayout(8, 0));
        lblPedidoSelecionado.setText("Selecione um pedido para visualizar detalhes e executar ações.");
        lblPedidoSelecionado.setForeground(new Color(90, 90, 90));
        pnlPedidoSelecionado.add(lblPedidoSelecionado, java.awt.BorderLayout.CENTER);

        pnlBotoesRodape.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 12, 5));

        btnVisualizarPedido.setText("Visualizar pedido");
        btnVisualizarPedido.setPreferredSize(new java.awt.Dimension(170, 32));
        pnlBotoesRodape.add(btnVisualizarPedido);

        btnExecutarAcao.setText("Executar ação");
        btnExecutarAcao.setPreferredSize(new java.awt.Dimension(170, 32));
        pnlBotoesRodape.add(btnExecutarAcao);

        btnFechar.setText("Fechar");
        btnFechar.setPreferredSize(new java.awt.Dimension(140, 32));
        pnlBotoesRodape.add(btnFechar);

        pnlPedidoSelecionado.add(pnlBotoesRodape, java.awt.BorderLayout.EAST);
        getContentPane().add(pnlPedidoSelecionado, java.awt.BorderLayout.SOUTH);

        pack();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public javax.swing.JFrame getJanelaPrincipal() {
        return this;
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
    public javax.swing.JSpinner getSpnData() {
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtualizarPainel;
    private javax.swing.JButton btnExecutarAcao;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnVisualizarPedido;
    private javax.swing.JComboBox<String> cmbPeriodo;
    private javax.swing.JLabel lblBuscarPedidoCliente;
    private javax.swing.JLabel lblNotificacao;
    private javax.swing.JLabel lblPedidoSelecionado;
    private javax.swing.JLabel lblPeriodo;
    private javax.swing.JLabel lblTituloCancelado;
    private javax.swing.JLabel lblTituloEmPreparo;
    private javax.swing.JLabel lblTituloEmRotaDeEntrega;
    private javax.swing.JLabel lblTituloEntregue;
    private javax.swing.JLabel lblTituloNovo;
    private javax.swing.JLabel lblTituloProntoParaEntrega;
    private javax.swing.JPanel pnlAcompanhamento;
    private javax.swing.JPanel pnlBotoesRodape;
    private javax.swing.JPanel pnlColunaCancelado;
    private javax.swing.JPanel pnlColunaEmPreparo;
    private javax.swing.JPanel pnlColunaEmRotaDeEntrega;
    private javax.swing.JPanel pnlColunaEntregue;
    private javax.swing.JPanel pnlColunaNovo;
    private javax.swing.JPanel pnlColunaProntoParaEntrega;
    private javax.swing.JPanel pnlColunas;
    private javax.swing.JPanel pnlFiltros;
    private javax.swing.JPanel pnlPedidoSelecionado;
    private javax.swing.JPanel pnlPedidosCancelado;
    private javax.swing.JPanel pnlPedidosEmPreparo;
    private javax.swing.JPanel pnlPedidosEmRotaDeEntrega;
    private javax.swing.JPanel pnlPedidosEntregue;
    private javax.swing.JPanel pnlPedidosNovo;
    private javax.swing.JPanel pnlPedidosProntoParaEntrega;
    private javax.swing.JScrollPane scrCancelado;
    private javax.swing.JScrollPane scrEmPreparo;
    private javax.swing.JScrollPane scrEmRotaDeEntrega;
    private javax.swing.JScrollPane scrEntregue;
    private javax.swing.JScrollPane scrNovo;
    private javax.swing.JScrollPane scrProntoParaEntrega;
    private javax.swing.JTextField txtBuscaPedidoCliente;
    private javax.swing.JLabel lblData;
    private javax.swing.JSpinner spnData;
    // End of variables declaration//GEN-END:variables
}
