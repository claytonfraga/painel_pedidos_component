package br.projeto.painelpedidos.som;

import java.awt.Toolkit;

/**
 *
 * @author clayton
 */
public class SomToolkitStrategy implements IEstrategiaSom {

    @Override
    public void tocar() {
        Toolkit.getDefaultToolkit().beep();
    }
}
