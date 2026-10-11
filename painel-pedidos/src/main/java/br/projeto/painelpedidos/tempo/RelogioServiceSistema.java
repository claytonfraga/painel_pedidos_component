package br.projeto.painelpedidos.tempo;

import java.time.LocalDateTime;

/**
 *
 * @author clayton
 */
public final class RelogioServiceSistema implements IRelogioService {

    @Override
    public LocalDateTime agora() {
        return LocalDateTime.now();
    }
}
