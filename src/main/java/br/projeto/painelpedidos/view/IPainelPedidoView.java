package br.projeto.painelpedidos.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 *
 * @author clayton
 */
public interface IPainelPedidoView {

    JFrame getJanelaPrincipal();

    JButton getBtnAtualizarPainel();

    JButton getBtnExecutarAcao();

    JButton getBtnFechar();

    JButton getBtnVisualizarPedido();

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
