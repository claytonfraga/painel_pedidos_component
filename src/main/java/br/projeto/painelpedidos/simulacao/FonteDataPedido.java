package br.projeto.painelpedidos.simulacao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author clayton
 */
public class FonteDataPedido implements IFonteDataPedido {

    private final IRelogioService relogioService;
    private LocalDate dataEspecifica;

    public FonteDataPedido(IRelogioService relogioService) {
        this.relogioService = Objects.requireNonNull(relogioService, "relogioService");
    }

    @Override
    public void usarDataAtual() {
        this.dataEspecifica = null;
    }

    @Override
    public void usarData(LocalDate data) {
        this.dataEspecifica = Objects.requireNonNull(data, "data");
    }

    @Override
    public LocalDateTime obterDataHora() {
        LocalDateTime agora = relogioService.agora();
        if (dataEspecifica == null) {
            return agora;
        }
        return dataEspecifica.atTime(agora.toLocalTime());
    }
}
