package br.projeto.painelpedidos.model;

/**
 *
 * @author clayton
 */
public class Cliente {

    private final String nome;
    private final String tipo;
    private double fidelidade;
    private final String logradouro;
    private final String bairro;
    private final String cidade;

    public Cliente(String nome, String tipo, double fidelidade, String logradouro, String bairro, String cidade) {
        this.nome = nome;
        this.tipo = tipo;
        this.fidelidade = fidelidade;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public double getFidelidade() {
        return fidelidade;
    }

    public void setFidelidade(double fidelidade) {
        this.fidelidade = fidelidade;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    @Override
    public String toString() {
        return "Cliente{nome='" + nome + "', tipo='" + tipo + "', fidelidade=" + fidelidade
                + ", logradouro='" + logradouro + "', bairro='" + bairro + "', cidade='" + cidade + "'}";
    }
}
