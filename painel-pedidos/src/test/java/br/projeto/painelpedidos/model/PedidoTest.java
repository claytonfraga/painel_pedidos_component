package br.projeto.painelpedidos.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * Ciclo de vida do pedido (BR12) e cancelamento com credito (BR13).
 *
 * @author clayton
 */
public final class PedidoTest {

    private static final LocalDateTime REFERENCIA = LocalDateTime.of(2026, 1, 10, 12, 0);

    @Test
    public void pedidoRecemCriadoEstaNovoESemPagamento() {
        // given: um pedido recem-criado
        PedidoAbstract pedido = criarPedido("1", 50.0);

        // when: nenhuma acao e executada

        // then: ele esta na situacao Novo e sem pagamento registrado
        assertThat(pedido.estaNovo(), is(true));
        assertThat(pedido.isPagamentoRegistrado(), is(false));
        assertThat(pedido.getSituacao(), is("Novo"));
    }

    @Test
    public void registrarPagamentoLevaNovoParaEmPreparo() {
        // given: um pedido Novo
        PedidoAbstract pedido = criarPedido("1", 50.0);

        // when: o pagamento e registrado
        pedido.registrarPagamento("Pix - chave CPF");

        // then: o pedido avanca para Em preparo com pagamento confirmado
        assertThat(pedido.estaEmPreparo(), is(true));
        assertThat(pedido.isPagamentoRegistrado(), is(true));
    }

    @Test
    public void progressaoCompletaLevaAteEntregue() {
        // given: um pedido Novo
        PedidoAbstract pedido = criarPedido("1", 50.0);

        // when: percorre-se todo o ciclo operacional
        pedido.registrarPagamento("Pix - chave CPF");
        pedido.marcarProntoParaEntrega();
        pedido.sairParaEntrega();
        pedido.marcarEntregue();

        // then: o pedido esta Entregue
        assertThat(pedido.estaEntregue(), is(true));
    }

    @Test
    public void cancelarSemPagamentoNaoGeraCredito() {
        // given: um pedido Novo sem pagamento
        PedidoAbstract pedido = criarPedido("1", 50.0);

        // when: o pedido e cancelado
        pedido.cancelar();

        // then: ele esta Cancelado e sem credito
        assertThat(pedido.estaCancelado(), is(true));
        assertThat(pedido.getCreditoGerado(), is(closeTo(0.0, 0.0001)));
    }

    @Test
    public void cancelarComPagamentoGeraCreditoIgualAoValorPago() {
        // given: um pedido pago de 73,45
        PedidoAbstract pedido = criarPedido("1", 73.45);
        pedido.registrarPagamento("Cartão de crédito");

        // when: o pedido e cancelado
        pedido.cancelar();

        // then: ele esta Cancelado e o credito e igual ao valor pago
        assertThat(pedido.estaCancelado(), is(true));
        assertThat(pedido.getCreditoGerado(), is(closeTo(73.45, 0.0001)));
    }

    @Test
    public void cancelarPedidoEntregueEPermitido() {
        // given: um pedido que ja foi entregue
        PedidoAbstract pedido = criarPedido("1", 50.0);
        pedido.registrarPagamento("Pix - chave CPF");
        pedido.marcarProntoParaEntrega();
        pedido.sairParaEntrega();
        pedido.marcarEntregue();

        // when: o pedido entregue e cancelado
        pedido.cancelar();

        // then: ele esta Cancelado com o credito do valor pago
        assertThat(pedido.estaCancelado(), is(true));
        assertThat(pedido.getCreditoGerado(), is(closeTo(50.0, 0.0001)));
    }

    @Test
    public void canceladoNaoRetornaParaNenhumaSituacaoAnterior() {
        // given: um pedido Cancelado
        PedidoAbstract pedido = criarPedido("1", 50.0);
        pedido.cancelar();

        // when: tenta-se registrar pagamento em um pedido Cancelado
        Executable registrarPagamento = new Executable() {
            @Override
            public void execute() {
                pedido.registrarPagamento("Pix - chave CPF");
            }
        };

        // then: a transicao e recusada com excecao
        assertThrows(RuntimeException.class, registrarPagamento);
    }

    @Test
    public void copiarCanceladoGeraNovoPedidoNovoSemPagamento() {
        // given: um pedido Cancelado
        PedidoAbstract pedido = criarPedido("1", 50.0);
        pedido.registrarPagamento("Pix - chave CPF");
        pedido.cancelar();

        // when: cria-se um novo pedido a partir do cancelado
        PedidoAbstract novo = pedido.copiarComoNovo("2", REFERENCIA.plusHours(1));

        // then: o novo pedido tem novo numero, situacao Novo e sem pagamento
        assertThat(novo.getIdentificador(), is("2"));
        assertThat(novo.estaNovo(), is(true));
        assertThat(novo.isPagamentoRegistrado(), is(false));
        assertThat(novo.getNomeCliente(), is("Ana Souza"));
        assertThat(novo.getValorTotal(), is(closeTo(50.0, 0.0001)));
    }

    @Test
    public void copiarPedidoNaoCanceladoFalha() {
        // given: um pedido Novo (nao cancelado)
        PedidoAbstract pedido = criarPedido("1", 50.0);

        // when: tenta-se copiar um pedido nao cancelado
        Executable copiar = new Executable() {
            @Override
            public void execute() {
                pedido.copiarComoNovo("2", REFERENCIA.plusHours(1));
            }
        };

        // then: a copia e recusada com excecao
        assertThrows(RuntimeException.class, copiar);
    }

    @Test
    public void pagamentoEmPedidoCanceladoOrientaOndeRegistrar() {
        // given: um pedido cancelado
        PedidoAbstract pedido = criarPedido("1", 50.0);
        pedido.cancelar();

        // when: tenta-se registrar o pagamento
        Executable registrar = new Executable() {
            @Override
            public void execute() {
                pedido.registrarPagamento("Pix - chave CPF");
            }
        };

        // then: a mensagem diz onde o pagamento e valido
        RuntimeException excecao = assertThrows(RuntimeException.class, registrar);
        assertThat(excecao.getMessage(), containsString("registre o pagamento em um pedido Novo"));
    }

    private PedidoAbstract criarPedido(String identificador, double valor) {
        return new PedidoDeTeste(identificador, REFERENCIA, "Ana Souza", "Rua das Flores, 120 - Centro", valor);
    }
}
