package br.projeto.painelpedidos.model;

import java.time.LocalDate;

/**
 *
 * @author clayton
 */
public class CupomDescontoPedido {

    private final String nomeCupom;
    private final double percentualDesconto;
    private final LocalDate dataLimite;
    private double valorDesconto;

    public CupomDescontoPedido(String nomeCupom, double percentualDesconto, LocalDate dataLimite) {
        if (percentualDesconto <= 0 || percentualDesconto > 1) {
            throw new IllegalArgumentException(
                    "Percentual de desconto invalido " + percentualDesconto + ", deve ser entre 0 e 1");
        }
        if (dataLimite != null && dataLimite.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Data limite invalida " + dataLimite + ", deve ser maior que a data atual " + LocalDate.now());
        }
        this.nomeCupom = nomeCupom;
        this.percentualDesconto = percentualDesconto;
        this.dataLimite = dataLimite;
        this.valorDesconto = 0;
    }

    public String getNomeMetodo() {
        return nomeCupom;
    }

    public double getPercentualDesconto() {
        return percentualDesconto;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public double getValorDesconto() {
        if (valorDesconto == 0) {
            throw new IllegalStateException("Valor desconto nao foi calculado");
        }
        return valorDesconto;
    }

    public void setValorDesconto(double valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    @Override
    public String toString() {
        return "CupomDescontoPedido{nomeCupom=" + nomeCupom + ", percentualDesconto=" + percentualDesconto
                + ", dataLimite=" + dataLimite + ", valorDesconto=" + valorDesconto + "}";
    }
}
