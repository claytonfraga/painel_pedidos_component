package br.projeto.painelpedidos.view;

import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * Janela Swing que hospeda o {@link PainelPedidoPanel}. E apenas um invólucro:
 * toda a composicao visual vive no painel embedavel.
 *
 * @author clayton
 */
public class PainelPedidoView extends JFrame implements IPainelPedidoView {

    private final PainelPedidoPanel painel;

    public PainelPedidoView(PainelPedidoPanel painel, String titulo) {
        super(titulo);
        this.painel = painel;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 760));
        setPreferredSize(new Dimension(1380, 980));
        setContentPane(painel);
        pack();
    }

    @Override
    public JPanel getPainelPrincipal() {
        return painel;
    }

    @Override
    public void maximizar() {
        painel.maximizar();
    }

    @Override
    public JButton getBtnAtualizarPainel() {
        return painel.getBtnAtualizarPainel();
    }

    @Override
    public JButton getBtnExecutarAcao() {
        return painel.getBtnExecutarAcao();
    }

    @Override
    public JButton getBtnFechar() {
        return painel.getBtnFechar();
    }

    @Override
    public JButton getBtnVisualizarPedido() {
        return painel.getBtnVisualizarPedido();
    }

    @Override
    public JButton getBtnNovoPedido() {
        return painel.getBtnNovoPedido();
    }

    @Override
    public JTextField getTxtBuscaPedidoCliente() {
        return painel.getTxtBuscaPedidoCliente();
    }

    @Override
    public JLabel getLblNotificacao() {
        return painel.getLblNotificacao();
    }

    @Override
    public JComboBox<String> getCmbPeriodo() {
        return painel.getCmbPeriodo();
    }

    @Override
    public JLabel getLblData() {
        return painel.getLblData();
    }

    @Override
    public JSpinner getSpnData() {
        return painel.getSpnData();
    }

    @Override
    public JPanel getPnlPedidosNovo() {
        return painel.getPnlPedidosNovo();
    }

    @Override
    public JPanel getPnlPedidosEmPreparo() {
        return painel.getPnlPedidosEmPreparo();
    }

    @Override
    public JPanel getPnlPedidosProntoParaEntrega() {
        return painel.getPnlPedidosProntoParaEntrega();
    }

    @Override
    public JPanel getPnlPedidosEmRotaDeEntrega() {
        return painel.getPnlPedidosEmRotaDeEntrega();
    }

    @Override
    public JPanel getPnlPedidosEntregue() {
        return painel.getPnlPedidosEntregue();
    }

    @Override
    public JPanel getPnlPedidosCancelado() {
        return painel.getPnlPedidosCancelado();
    }

    @Override
    public JLabel getLblPedidoSelecionado() {
        return painel.getLblPedidoSelecionado();
    }

    @Override
    public JLabel getLblTituloNovo() {
        return painel.getLblTituloNovo();
    }

    @Override
    public JLabel getLblTituloEmPreparo() {
        return painel.getLblTituloEmPreparo();
    }

    @Override
    public JLabel getLblTituloProntoParaEntrega() {
        return painel.getLblTituloProntoParaEntrega();
    }

    @Override
    public JLabel getLblTituloEmRotaDeEntrega() {
        return painel.getLblTituloEmRotaDeEntrega();
    }

    @Override
    public JLabel getLblTituloEntregue() {
        return painel.getLblTituloEntregue();
    }

    @Override
    public JLabel getLblTituloCancelado() {
        return painel.getLblTituloCancelado();
    }
}
