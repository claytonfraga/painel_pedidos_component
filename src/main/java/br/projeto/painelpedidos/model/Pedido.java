package br.projeto.painelpedidos.model;

import br.projeto.painelpedidos.configuracao.ConfiguracaoService;
import br.projeto.painelpedidos.state.PedidoNovoState;
import br.projeto.painelpedidos.state.PedidoState;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public class Pedido {

    private final ConfiguracaoService configuracaoService = new ConfiguracaoService();
    private double taxaEntrega = configuracaoService.getTaxaEntregaPadrao();
    private final List<Item> itens = new ArrayList<>();
    private final Cliente cliente;
    private final List<CupomDescontoEntrega> cuponsDescontoEntrega = new ArrayList<>();
    private CupomDescontoPedido cupomPedidoAplicado;

    private final String identificador;
    private final LocalDateTime dataHora;
    private final String enderecoEntrega;
    private final double valorTotal;
    private String formaPagamento;
    private LocalDateTime dataHoraPagamento;
    private PedidoState estado;
    private double creditoGerado;
    private LocalDateTime dataHoraMudancaEstado;
    private boolean prioridade;

    public Pedido(
            String identificador,
            LocalDateTime dataHora,
            String nomeCliente,
            String enderecoEntrega,
            double valorTotal) {

        this.identificador = validarTextoObrigatorio(identificador, "identificador");
        this.dataHora = Objects.requireNonNull(dataHora, "dataHora");
        this.cliente = new Cliente(nomeCliente, "Nao informado", 0.0, enderecoEntrega, "Nao informado", "Nao informado");
        this.enderecoEntrega = validarTextoObrigatorio(enderecoEntrega, "enderecoEntrega");
        this.valorTotal = validarValorTotal(valorTotal);
        this.creditoGerado = 0.0;
        this.prioridade = false;
        this.dataHoraMudancaEstado = this.dataHora;
        this.estado = new PedidoNovoState(this);
    }

    public void registrarPagamento(String formaPagamento) {
        estado.registrarPagamento(formaPagamento);
    }

    public void marcarProntoParaEntrega() {
        estado.marcarProntoParaEntrega();
    }

    public void sairParaEntrega() {
        estado.sairParaEntrega();
    }

    public void marcarEntregue() {
        estado.marcarEntregue();
    }

    public void cancelar() {
        estado.cancelar();
    }

    public void aplicarPagamento(String formaPagamento) {
        this.formaPagamento = validarTextoObrigatorio(formaPagamento, "formaPagamento");
        this.dataHoraPagamento = LocalDateTime.now();
    }

    public void confirmarCancelamento() {
        creditoGerado = isPagamentoRegistrado() ? valorTotal : 0.0;
    }

    public Pedido copiarComoNovo(String novoIdentificador, LocalDateTime novaDataHora) {
        if (!estado.ehCancelado()) {
            throw new RuntimeException("Nao e possivel copiar um pedido " + estado.getNome());
        }
        return new Pedido(novoIdentificador, novaDataHora, getNomeCliente(), enderecoEntrega, valorTotal);
    }

    public void adicionarItem(Item objeto) {
        if (objeto == null) {
            throw new IllegalArgumentException("Item do pedido deve ser informado");
        }
        itens.add(objeto);
    }

    public double getValorPedido() {
        double valor = 0;
        for (Item item : itens) {
            valor += item.getValorTotal();
        }
        return valor;
    }

    public double getTaxaEntrega() {
        return taxaEntrega;
    }

    public double getTotalDescontosTaxaEntrega() {
        double desconto = 0;
        for (CupomDescontoEntrega cupom : cuponsDescontoEntrega) {
            desconto += cupom.getValorDesconto();
        }
        return desconto;
    }

    public void adicionarCupomDescontoEntrega(CupomDescontoEntrega cupom) {
        if (cupom == null) {
            throw new IllegalArgumentException("Cupom de desconto da entrega deve ser informado");
        }
        if (cupom.getValorDesconto() < 0) {
            throw new IllegalArgumentException("Desconto na taxa de entrega nao pode ser negativo");
        }

        double totalDescontosAposAdicionar = getTotalDescontosTaxaEntrega() + cupom.getValorDesconto();
        if (totalDescontosAposAdicionar > taxaEntrega) {
            throw new IllegalStateException(
                    "Desconto total na taxa de entrega nao pode ultrapassar " + taxaEntrega);
        }
        cuponsDescontoEntrega.add(cupom);
    }

    public double getTaxaEntregaComDesconto() {
        double taxaComDesconto = taxaEntrega - getTotalDescontosTaxaEntrega();
        if (taxaComDesconto < 0) {
            return 0;
        }
        return taxaComDesconto;
    }

    public double calcularValorTotal() {
        double valorCalculado = getValorPedido() + getTaxaEntregaComDesconto();
        if (cupomPedidoAplicado != null) {
            valorCalculado = valorCalculado - valorCalculado * cupomPedidoAplicado.getPercentualDesconto();
        }
        return valorCalculado;
    }

    public boolean isPagamentoRegistrado() {
        return formaPagamento != null && dataHoraPagamento != null;
    }

    public boolean estaNovo() {
        return estado.ehNovo();
    }

    public boolean estaEmPreparo() {
        return estado.ehEmPreparo();
    }

    public boolean estaProntoParaEntrega() {
        return estado.ehProntoParaEntrega();
    }

    public boolean estaEmRotaDeEntrega() {
        return estado.ehEmRotaDeEntrega();
    }

    public boolean estaEntregue() {
        return estado.ehEntregue();
    }

    public boolean estaCancelado() {
        return estado.ehCancelado();
    }

    public PedidoState getEstado() {
        return estado;
    }

    public void setEstado(PedidoState estado) {
        this.estado = Objects.requireNonNull(estado, "estado");
        this.dataHoraMudancaEstado = LocalDateTime.now();
        this.prioridade = false;
    }

    public void marcarPrioridade() {
        this.prioridade = true;
    }

    public boolean isPrioridade() {
        return prioridade;
    }

    public LocalDateTime getDataHoraMudancaEstado() {
        return dataHoraMudancaEstado;
    }

    public String getBairroEntrega() {
        int separador = enderecoEntrega.lastIndexOf(" - ");
        if (separador < 0) {
            return enderecoEntrega;
        }
        return enderecoEntrega.substring(separador + 3).trim();
    }

    public String getIdentificador() {
        return identificador;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getNomeCliente() {
        return cliente.getNome();
    }

    public List<Item> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public LocalDateTime getDataHoraPagamento() {
        return dataHoraPagamento;
    }

    public String getSituacao() {
        return estado.getNome();
    }

    public double getCreditoGerado() {
        return creditoGerado;
    }

    private double validarValorTotal(double valorInformado) {
        if (valorInformado < 0) {
            throw new IllegalArgumentException("O valor total do pedido nao pode ser negativo");
        }
        return valorInformado;
    }

    private String validarTextoObrigatorio(String valor, String nomeCampo) {
        String texto = Objects.requireNonNull(valor, nomeCampo).trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException(nomeCampo + " deve ser informado");
        }
        return texto;
    }

    @Override
    public String toString() {
        return "Pedido{"
                + "identificador=" + identificador
                + ", dataHora=" + dataHora
                + ", cliente=" + cliente
                + ", enderecoEntrega=" + enderecoEntrega
                + ", valorTotal=" + valorTotal
                + ", formaPagamento=" + formaPagamento
                + ", dataHoraPagamento=" + dataHoraPagamento
                + ", situacao=" + getSituacao()
                + ", creditoGerado=" + creditoGerado
                + "}";
    }
}
