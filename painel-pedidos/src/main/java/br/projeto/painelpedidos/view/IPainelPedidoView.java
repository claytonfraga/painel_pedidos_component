package br.projeto.painelpedidos.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 * Abstracao da View (MVP Passive View). Expoe o painel Swing embedavel e os
 * elementos que a Presenter manipula, sem acoplar a Presenter a uma janela.
 *
 * @author clayton
 */
public interface IPainelPedidoView {

    JPanel getPainelPrincipal();

    void maximizar();

    JButton getBtnAtualizarPainel();

    JButton getBtnExecutarAcao();

    JButton getBtnFechar();

    JButton getBtnVisualizarPedido();

    JButton getBtnNovoPedido();

    JTextField getTxtBuscaPedidoCliente();

    JLabel getLblNotificacao();

    JComboBox<String> getCmbPeriodo();

    JLabel getLblData();

    JSpinner getSpnData();

    JPanel getPnlPedidosNovo();

    JPanel getPnlPedidosEmPreparo();

    JPanel getPnlPedidosProntoParaEntrega();

    JPanel getPnlPedidosEmRotaDeEntrega();

    JPanel getPnlPedidosEntregue();

    JPanel getPnlPedidosCancelado();

    JLabel getLblPedidoSelecionado();

    JLabel getLblTituloNovo();

    JLabel getLblTituloEmPreparo();

    JLabel getLblTituloProntoParaEntrega();

    JLabel getLblTituloEmRotaDeEntrega();

    JLabel getLblTituloEntregue();

    JLabel getLblTituloCancelado();
}
