package br.projeto.painelpedidos.internal.filtro;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

/**
 * Filtro de periodo: a fabrica e a fonte unica da lista de rotulos e do
 * mapeamento indice -> periodo, para a tela nao divergir da ordem.
 *
 * @author clayton
 */
public final class FabricaFiltroPeriodoTest {

    private static final LocalDate HOJE = LocalDate.of(2026, 1, 10);

    private final FabricaFiltroPeriodo fabrica = new FabricaFiltroPeriodo();

    @Test
    public void rotulosDaLinhaDeFiltroVemDaFabrica() {
        // given: a fabrica de periodos

        // when: pedem-se os rotulos

        // then: sao os cinco periodos, com o primeiro citando hoje
        assertThat(fabrica.rotulos(HOJE), hasSize(5));
        assertThat(fabrica.rotulos(HOJE).get(0), containsString("Hoje (10/01/2026)"));
    }

    @Test
    public void somenteOUltimoPeriodoExigeData() {
        // given: a fabrica de periodos

        // when: consulta-se quem exige data

        // then: so o indice da data especifica
        assertThat(fabrica.exigeData(4), is(true));
        assertThat(fabrica.exigeData(0), is(false));
        assertThat(fabrica.exigeData(3), is(false));
    }

    @Test
    public void cadaIndiceDevolveOFiltroCorrespondente() {
        // given: uma data de referencia e um pedido dela
        LocalDateTime pedidoDeHoje = HOJE.atTime(12, 0);

        // when: criam-se os filtros pelos indices

        // then: o primeiro aceita so hoje e o de data especifica aceita a data dada
        assertThat(fabrica.criar(0, null).aceita(pedidoDeHoje, HOJE), is(true));
        assertThat(fabrica.criar(0, null).aceita(pedidoDeHoje.minusDays(1), HOJE), is(false));
        assertThat(fabrica.criar(4, HOJE).aceita(pedidoDeHoje, HOJE), is(true));
        assertThat(fabrica.criar(3, null).aceita(pedidoDeHoje.minusYears(1), HOJE), is(true));
    }
}
