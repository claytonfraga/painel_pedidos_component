package br.projeto.painelpedidos.model;

/**
 *
 * @author clayton
 */
public class Item {

    private final String nome;
    private final int quantidade;
    private final double valorUnitario;
    private final String tipo;

    public Item(String nome, int quantidade, double valorUnitario, String tipo) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public String getTipo() {
        return tipo;
    }

    public double getValorTotal() {
        return valorUnitario * quantidade;
    }

    @Override
    public String toString() {
        return "Item{nome='" + nome + "', quantidade=" + quantidade
                + ", valorUnitario=" + valorUnitario + ", tipo='" + tipo
                + "', valorTotal=" + getValorTotal() + "}";
    }
}
