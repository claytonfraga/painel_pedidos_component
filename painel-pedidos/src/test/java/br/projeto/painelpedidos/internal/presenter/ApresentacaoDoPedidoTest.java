package br.projeto.painelpedidos.internal.presenter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import br.projeto.painelpedidos.model.PedidoAbstract;
import br.projeto.painelpedidos.model.PedidoDeTeste;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

/**
 * Aparencia do pedido por situacao: o texto da acao, a cor da raia e o rotulo
 * de status mudam com o estado, e o credito so aparece no cancelado com
 * pagamento.
 *
 * @author clayton
 */
public final class ApresentacaoDoPedidoTest {

    private static final LocalDateTime REFERENCIA = LocalDateTime.of(2026, 1, 10, 12, 0);

    private final ApresentacaoDoPedido apresentacao = new ApresentacaoDoPedido();

    @Test
    public void pedidoNovoPedeConfirmacaoDePagamentoEAguardaPagamento() {
        // given: um pedido recem-criado
        PedidoAbstract pedido = criarPedido("1", 50.0);

        // when: consultam-se acao e status

        // then: a acao confirma o pagamento e o status aguarda
        assertThat(apresentacao.textoDaAcaoPrincipal(pedido), is("Confirmar pagamento"));
        assertThat(apresentacao.textoDoStatus(pedido), is("Aguardando pagamento"));
    }

    @Test
    public void pedidoPagoMudaAcaoEStatus() {
        // given: um pedido com pagamento registrado
        PedidoAbstract pedido = criarPedido("1", 50.0);
        pedido.registrarPagamento("Pix - chave CPF");

        // when: consultam-se acao e status

        // then: a acao avanca a producao e o status confirma o pagamento
        assertThat(apresentacao.textoDaAcaoPrincipal(pedido), is("Pronto pra entrega"));
        assertThat(apresentacao.textoDoStatus(pedido), is("Pagamento confirmado"));
    }

    @Test
    public void pedidoCanceladoOfereceNovoPedidoESemCreditoSemPagamento() {
        // given: um pedido cancelado sem pagamento
        PedidoAbstract pedido = criarPedido("1", 50.0);
        pedido.cancelar();

        // when: consultam-se acao, status e credito

        // then: a acao cria um novo pedido e nao ha credito
        assertThat(apresentacao.textoDaAcaoPrincipal(pedido), is("Criar novo pedido"));
        assertThat(apresentacao.textoDoStatus(pedido), is("Pedido cancelado"));
        assertThat(apresentacao.possuiCreditoGerado(pedido), is(false));
        assertThat(apresentacao.textoDoCredito(pedido, "R$ 0,00"), is("Sem crédito gerado"));
    }

    @Test
    public void pedidoCanceladoComPagamentoGeraCreditoDoValorPago() {
        // given: um pedido pago e depois cancelado
        PedidoAbstract pedido = criarPedido("1", 42.90);
        pedido.registrarPagamento("Pix - chave CPF");
        pedido.cancelar();

        // when: consulta-se o credito

        // then: o credito aparece com o valor formatado recebido
        assertThat(apresentacao.possuiCreditoGerado(pedido), is(true));
        assertThat(apresentacao.textoDoCredito(pedido, "R$ 42,90"),
                containsString("R$ 42,90"));
    }

    @Test
    public void aCorDaRaiaDistingueOsEstados() {
        // given: um pedido novo e outro entregue
        PedidoAbstract novo = criarPedido("1", 50.0);
        PedidoAbstract entregue = criarPedido("2", 50.0);
        entregue.registrarPagamento("Pix - chave CPF");
        entregue.marcarProntoParaEntrega();
        entregue.sairParaEntrega();
        entregue.marcarEntregue();

        // when: comparam-se as cores de fundo

        // then: cada estado tem a sua cor
        assertThat(apresentacao.corDeFundoDaRaia(novo).equals(apresentacao.corDeFundoDaRaia(entregue)),
                is(false));
    }

    private PedidoAbstract criarPedido(String identificador, double valor) {
        return new PedidoDeTeste(
                identificador, REFERENCIA, "Ana Souza", "Rua das Flores, 120 - Centro", valor);
    }
}
