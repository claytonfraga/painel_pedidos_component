package br.projeto.painelpedidos.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Modelo abstrato de pedido do componente. Concentra apenas o que o painel e a
 * maquina de estados (GoF State) precisam. O componente NAO possui pedido
 * concreto: o sistema consumidor estende esta classe e implementa
 * {@link #criarCopia(String, LocalDateTime)} com o seu proprio tipo.
 *
 * @author clayton
 */
public abstract class PedidoAbstract {

    private final String identificador;
    private final LocalDateTime dataHora;
    private final String nomeCliente;
    private final String enderecoEntrega;
    private final double valorTotal;

    private String formaPagamento;
    private LocalDateTime dataHoraPagamento;
    private PedidoState estado;
    private double creditoGerado;
    private LocalDateTime dataHoraMudancaEstado;
    private boolean prioridade;

    protected PedidoAbstract(
            String identificador,
            LocalDateTime dataHora,
            String nomeCliente,
            String enderecoEntrega,
            double valorTotal) {

        this.identificador = validarTextoObrigatorio(identificador, "o identificador do pedido");
        this.dataHora = Objects.requireNonNull(dataHora, "Informe uma data e hora do pedido valida");
        this.nomeCliente = validarTextoObrigatorio(nomeCliente, "o nome do cliente");
        this.enderecoEntrega = validarTextoObrigatorio(enderecoEntrega, "o endereco de entrega");
        this.valorTotal = validarValorTotal(valorTotal);
        this.creditoGerado = 0.0;
        this.prioridade = false;
        this.dataHoraMudancaEstado = this.dataHora;
        this.estado = new PedidoNovoState(this);
    }

    /**
     * Cria uma copia do pedido como novo (sem pagamento), no tipo concreto do
     * cliente. Chamada por {@link #copiarComoNovo(String, LocalDateTime)}.
     */
    public abstract PedidoAbstract criarCopia(String novoIdentificador, LocalDateTime novaDataHora);

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
        this.formaPagamento = validarTextoObrigatorio(formaPagamento, "a forma de pagamento");
        this.dataHoraPagamento = LocalDateTime.now();
    }

    public void confirmarCancelamento() {
        creditoGerado = isPagamentoRegistrado() ? valorTotal : 0.0;
    }

    public PedidoAbstract copiarComoNovo(String novoIdentificador, LocalDateTime novaDataHora) {
        if (!estado.ehCancelado()) {
            throw new RuntimeException("Nao e possivel copiar um pedido " + estado.getNome()
                    + "; copie apenas um pedido Cancelado");
        }
        return criarCopia(novoIdentificador, novaDataHora);
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

    void setEstado(PedidoState estado) {
        this.estado = Objects.requireNonNull(estado, "Informe um estado de pedido valido");
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

    public String getIdentificador() {
        return identificador;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getNomeCliente() {
        return nomeCliente;
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

    public String getSituacao() {
        return estado.getNome();
    }

    public double getCreditoGerado() {
        return creditoGerado;
    }

    private double validarValorTotal(double valorInformado) {
        if (valorInformado < 0) {
            throw new IllegalArgumentException(
                    "Informe um valor total de pedido igual ou maior que zero");
        }
        return valorInformado;
    }

    private String validarTextoObrigatorio(String valor, String descricao) {
        String texto = Objects.requireNonNull(valor, "Informe " + descricao).trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException(
                    "Informe " + descricao + " (o texto nao pode ser vazio)");
        }
        return texto;
    }
}
