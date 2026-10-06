package br.projeto.painelpedidos.seeder;

import br.projeto.painelpedidos.dados.CatalogoPedido;
import br.projeto.painelpedidos.model.Pedido;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public class PedidoSeeder {

    private final int quantidade;
    private final LocalDateTime dataReferencia;
    private final CatalogoPedido catalogoPedido;

    public PedidoSeeder(int quantidade, LocalDateTime dataReferencia, CatalogoPedido catalogoPedido) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade de pedidos nao pode ser negativa");
        }
        this.quantidade = quantidade;
        this.dataReferencia = Objects.requireNonNull(dataReferencia, "dataReferencia");
        this.catalogoPedido = Objects.requireNonNull(catalogoPedido, "catalogoPedido");
    }

    public List<Pedido> gerar() {
        List<Pedido> pedidos = new ArrayList<>();
        for (int indice = 0; indice < quantidade; indice++) {
            pedidos.add(criarPedido(indice));
        }
        return pedidos;
    }

    private Pedido criarPedido(int indice) {
        List<String> nomes = catalogoPedido.getNomes();
        List<String> sobrenomes = catalogoPedido.getSobrenomes();
        List<String> logradouros = catalogoPedido.getLogradouros();
        List<String> bairros = catalogoPedido.getBairros();
        List<String> formasPagamento = catalogoPedido.getFormasPagamento();

        String identificador = String.valueOf(3001 + indice);
        int variacao = indice % 6;
        LocalDateTime dataHora = variacao == 0 ? calcularDataHoraNovo(indice) : calcularDataHora(indice);
        String nome = nomes.get(indice % nomes.size()) + " "
                + sobrenomes.get((indice / nomes.size()) % sobrenomes.size());
        String endereco = "Rua " + logradouros.get(indice % logradouros.size())
                + ", " + (100 + indice) + " - " + bairros.get(indice % bairros.size());
        double valor = calcularValor(indice);
        String formaPagamento = formasPagamento.get(indice % formasPagamento.size());

        Pedido pedido = new Pedido(identificador, dataHora, nome, endereco, valor);
        aplicarSituacao(pedido, indice, formaPagamento);
        if (variacao == 0 && indice % 20 == 0) {
            pedido.marcarPrioridade();
        }
        return pedido;
    }

    private LocalDateTime calcularDataHoraNovo(int indice) {
        int minutos = indice % 20 == 0 ? 6 + indice % 5 : indice % 4;
        return dataReferencia
                .minusMinutes(minutos)
                .withSecond(0)
                .withNano(0);
    }

    private LocalDateTime calcularDataHora(int indice) {
        int mesesAtras = indice % 12;
        int diasAtras = (indice / 12) % 27;
        int hora = 8 + indice % 12;
        int minuto = (indice * 11) % 60;

        return dataReferencia
                .minusMonths(mesesAtras)
                .minusDays(diasAtras)
                .withHour(hora)
                .withMinute(minuto)
                .withSecond(0)
                .withNano(0);
    }

    private double calcularValor(int indice) {
        return 20.0 + indice % 130 + ((indice * 3) % 100) / 100.0;
    }

    private void aplicarSituacao(Pedido pedido, int indice, String formaPagamento) {
        int variacao = indice % 6;
        if (variacao == 1) {
            pedido.registrarPagamento(formaPagamento);
            return;
        }
        if (variacao == 2) {
            pedido.registrarPagamento(formaPagamento);
            pedido.marcarProntoParaEntrega();
            return;
        }
        if (variacao == 3) {
            pedido.registrarPagamento(formaPagamento);
            pedido.marcarProntoParaEntrega();
            pedido.sairParaEntrega();
            return;
        }
        if (variacao == 4) {
            pedido.registrarPagamento(formaPagamento);
            pedido.marcarProntoParaEntrega();
            pedido.sairParaEntrega();
            pedido.marcarEntregue();
            return;
        }
        if (variacao == 5) {
            if (indice % 2 == 0) {
                pedido.registrarPagamento(formaPagamento);
            }
            pedido.cancelar();
        }
    }
}
