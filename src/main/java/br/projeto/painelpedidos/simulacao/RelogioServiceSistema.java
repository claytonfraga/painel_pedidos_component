package br.projeto.painelpedidos.simulacao;

import java.time.LocalDateTime;

/**
 *
 * @author clayton
 */
public class RelogioServiceSistema implements IRelogioService {

    @Override
    public LocalDateTime agora() {
        return LocalDateTime.now();
    }
}
