package br.projeto.painelpedidos.simulacao;

import br.projeto.painelpedidos.dados.CatalogoPedido;
import br.projeto.painelpedidos.model.Pedido;
import br.projeto.painelpedidos.repositorio.IPedidoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public class GeradorPedidoSimulado implements IGeradorPedido {

    private final IPedidoRepository pedidoRepository;
    private final IFonteDataPedido fonteDataPedido;
    private final IAleatoriedadeService aleatoriedadeService;
    private final CatalogoPedido catalogoPedido;

    public GeradorPedidoSimulado(
            IPedidoRepository pedidoRepository,
            IFonteDataPedido fonteDataPedido,
            IAleatoriedadeService aleatoriedadeService,
            CatalogoPedido catalogoPedido) {

        this.pedidoRepository = Objects.requireNonNull(pedidoRepository, "pedidoRepository");
        this.fonteDataPedido = Objects.requireNonNull(fonteDataPedido, "fonteDataPedido");
        this.aleatoriedadeService = Objects.requireNonNull(aleatoriedadeService, "aleatoriedadeService");
        this.catalogoPedido = Objects.requireNonNull(catalogoPedido, "catalogoPedido");
    }

    @Override
    public Pedido gerar() {
        String identificador = pedidoRepository.gerarProximoIdentificador();
        LocalDateTime dataHora = fonteDataPedido.obterDataHora();
        String nome = sortear(catalogoPedido.getNomes()) + " " + sortear(catalogoPedido.getSobrenomes());
        String endereco = "Rua " + sortear(catalogoPedido.getLogradouros())
                + ", " + (100 + aleatoriedadeService.sortearInteiro(0, 900))
                + " - " + sortear(catalogoPedido.getBairros());
        double valor = 20.0
                + aleatoriedadeService.sortearInteiro(0, 130)
                + aleatoriedadeService.sortearInteiro(0, 100) / 100.0;
        return new Pedido(identificador, dataHora, nome, endereco, valor);
    }

    private String sortear(List<String> valores) {
        return valores.get(aleatoriedadeService.sortearInteiro(0, valores.size()));
    }
}
