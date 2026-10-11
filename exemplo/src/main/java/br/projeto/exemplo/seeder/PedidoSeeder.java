package br.projeto.exemplo.seeder;

import br.projeto.exemplo.model.Pedido;
import br.projeto.painelpedidos.model.PedidoAbstract;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Seeder deterministico dos dados da atividade. Recebe a data de referencia e
 * deriva todas as datas dela; nao usa Random nem o relogio do sistema, e nao
 * gera pedidos fora da lista fixa.
 *
 * @author clayton
 */
public final class PedidoSeeder {

    private final LocalDateTime dataReferencia;

    public PedidoSeeder(LocalDateTime dataReferencia) {
        this.dataReferencia = Objects.requireNonNull(dataReferencia, "dataReferencia");
    }

    public List<PedidoAbstract> gerar() {
        List<PedidoAbstract> pedidos = new ArrayList<>();

        pedidos.add(novo("1001", dataReferencia.minusMinutes(3), "Ana Souza",
                "Rua das Flores, 120 - Centro", 42.90));
        pedidos.add(novo("1002", dataReferencia.minusMinutes(12), "Bruno Lima",
                "Av. da Paz, 480 - Jardim", 88.50));
        pedidos.add(novo("1003", dataReferencia.minusHours(6), "Carla Rocha",
                "Rua Nova, 35 - Vila do Sul", 31.00));

        pedidos.add(emPreparo("1004", dataReferencia.minusMinutes(40), "Daniel Almeida",
                "Rua das Palmeiras, 90 - Centro", 57.30));
        pedidos.add(emPreparo("1005", dataReferencia.minusHours(3), "Eduardo Mendes",
                "Rua do Comercio, 210 - Sao Miguel", 120.00));
        pedidos.add(emPreparo("1006", dataReferencia.minusDays(2), "Fernanda Costa",
                "Rua das Acacias, 55 - Bela Vista", 65.40));

        pedidos.add(pronto("1007", dataReferencia.minusMinutes(55), "Gabriel Ribeiro",
                "Rua das Rosas, 12 - Jardim", 74.90));
        pedidos.add(pronto("1008", dataReferencia.minusDays(4), "Helena Nunes",
                "Av. do Sol, 300 - Centro", 39.90));

        pedidos.add(emRota("1009", dataReferencia.minusMinutes(70), "Igor Gomes",
                "Rua Sao Miguel, 8 - Sao Miguel", 52.75));
        pedidos.add(emRota("1010", dataReferencia.minusDays(9), "Julia Martins",
                "Rua do Sol, 77 - Jardim", 46.20));

        pedidos.add(entregue("1011", dataReferencia.minusHours(5), "Karina Barros",
                "Rua das Flores, 400 - Centro", 98.00));
        pedidos.add(entregue("1012", dataReferencia.minusDays(15), "Lucas Ramos",
                "Av. da Paz, 150 - Vila do Sul", 33.60));
        pedidos.add(entregue("1013", dataReferencia.minusMonths(2), "Marina Teixeira",
                "Rua Nova, 640 - Bela Vista", 71.10));

        pedidos.add(cancelado("1014", dataReferencia.minusDays(20), "Nicolas Fernandes",
                "Rua das Palmeiras, 11 - Centro", 60.00, true));
        pedidos.add(cancelado("1015", dataReferencia.minusMonths(3), "Olivia Correia",
                "Rua do Comercio, 250 - Jardim", 45.50, false));

        return pedidos;
    }

    private PedidoAbstract novo(String id, LocalDateTime dataHora, String cliente, String endereco, double valor) {
        return new Pedido(id, dataHora, cliente, endereco, valor);
    }

    private PedidoAbstract emPreparo(String id, LocalDateTime dataHora, String cliente, String endereco, double valor) {
        PedidoAbstract pedido = new Pedido(id, dataHora, cliente, endereco, valor);
        pedido.registrarPagamento("Pix - chave CPF");
        return pedido;
    }

    private PedidoAbstract pronto(String id, LocalDateTime dataHora, String cliente, String endereco, double valor) {
        PedidoAbstract pedido = emPreparo(id, dataHora, cliente, endereco, valor);
        pedido.marcarProntoParaEntrega();
        return pedido;
    }

    private PedidoAbstract emRota(String id, LocalDateTime dataHora, String cliente, String endereco, double valor) {
        PedidoAbstract pedido = pronto(id, dataHora, cliente, endereco, valor);
        pedido.sairParaEntrega();
        return pedido;
    }

    private PedidoAbstract entregue(String id, LocalDateTime dataHora, String cliente, String endereco, double valor) {
        PedidoAbstract pedido = emRota(id, dataHora, cliente, endereco, valor);
        pedido.marcarEntregue();
        return pedido;
    }

    private PedidoAbstract cancelado(
            String id, LocalDateTime dataHora, String cliente, String endereco, double valor, boolean pago) {
        PedidoAbstract pedido = new Pedido(id, dataHora, cliente, endereco, valor);
        if (pago) {
            pedido.registrarPagamento("Cartão de crédito");
        }
        pedido.cancelar();
        return pedido;
    }
}
