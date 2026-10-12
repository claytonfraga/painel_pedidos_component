package br.projeto.painelpedidos.internal.presenter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import br.projeto.painelpedidos.model.PedidoAbstract;
import br.projeto.painelpedidos.model.PedidoDeTeste;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

/**
 * Formatacao de pedido para a tela: valor monetario em real, identificador com
 * prefixo e horario relativo ao dia.
 *
 * @author clayton
 */
public final class FormatacaoDePedidoTest {

    private final FormatacaoDePedido formatacao = new FormatacaoDePedido();

    @Test
    public void valorMonetarioSaiEmRealComEspacoNormal() {
        // given: um valor com centavos

        // when: o valor e formatado

        // then: sai no formato de real, sem espaco inquebravel
        String formatado = formatacao.valorMonetario(42.90);
        assertThat(formatado, containsString("42,90"));
        assertThat(formatado.contains("\u00A0"), is(false));
    }

    @Test
    public void identificadorSaiComPrefixoUmaVezSo() {
        // given: identificadores com e sem prefixo

        // when: sao formatados

        // then: o prefixo aparece uma unica vez
        assertThat(formatacao.identificador("7"), is("#7"));
        assertThat(formatacao.identificador("#7"), is("#7"));
    }

    @Test
    public void horarioDeHojeSaiSoComHoraEDeOntemComMarcador() {
        // given: um horario de hoje e um de ontem
        LocalDateTime agora = LocalDateTime.now().withHour(14).withMinute(5);

        // when: sao formatados

        // then: hoje sai so a hora e ontem vem marcado
        assertThat(formatacao.horario(agora, agora.toLocalDate()), is("14:05"));
        assertThat(formatacao.horario(agora.minusDays(1), agora.toLocalDate()), containsString("Ontem"));
    }

    @Test
    public void formaDePagamentoSoApareceQuandoRegistrada() {
        // given: um pedido sem e outro com pagamento
        PedidoAbstract semPagamento = criarPedido("1");
        PedidoAbstract comPagamento = criarPedido("2");
        comPagamento.registrarPagamento("Pix - chave CPF", LocalDateTime.of(2026, 1, 10, 12, 0));

        // when: consulta-se a forma de pagamento

        // then: vazia sem registro e preenchida com ele
        assertThat(formatacao.formaPagamento(semPagamento), is(""));
        assertThat(formatacao.formaPagamento(comPagamento), is("Pix - chave CPF"));
    }

    private PedidoAbstract criarPedido(String identificador) {
        return new PedidoDeTeste(
                identificador,
                LocalDateTime.of(2026, 1, 10, 12, 0),
                "Ana Souza",
                "Rua das Flores, 120 - Centro",
                50.0);
    }
}
