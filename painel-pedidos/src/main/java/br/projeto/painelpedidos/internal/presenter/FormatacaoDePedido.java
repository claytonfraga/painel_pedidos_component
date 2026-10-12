package br.projeto.painelpedidos.internal.presenter;

import br.projeto.painelpedidos.model.PedidoAbstract;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Formatacao de pedido para a tela: valor monetario, identificador e horario.
 * Extraida do presenter para ele nao acumular formatacao.
 *
 * @author clayton
 */
final class FormatacaoDePedido {

    private final NumberFormat formatadorMoeda;
    private final DateTimeFormatter formatadorHora;
    private final DateTimeFormatter formatadorDataHora;

    FormatacaoDePedido() {
        this.formatadorMoeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
        this.formatadorHora = DateTimeFormatter.ofPattern("HH:mm");
        this.formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM HH:mm");
    }

    String identificador(String identificador) {
        return identificador.startsWith("#") ? identificador : "#" + identificador;
    }

    String valorMonetario(double valor) {
        return formatadorMoeda.format(valor).replace('\u00A0', ' ');
    }

    String horario(LocalDateTime dataHora) {
        LocalDate dataPedido = dataHora.toLocalDate();
        LocalDate hoje = LocalDate.now();

        if (dataPedido.equals(hoje)) {
            return dataHora.format(formatadorHora);
        }
        if (dataPedido.equals(hoje.minusDays(1))) {
            return "Ontem " + dataHora.format(formatadorHora);
        }
        return dataHora.format(formatadorDataHora);
    }

    String formaPagamento(PedidoAbstract pedido) {
        if (!pedido.isPagamentoRegistrado()) {
            return "";
        }
        return pedido.getFormaPagamento();
    }
}
