package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

/**
 * Classe responsável por lidar com eventos de teclado durante o jogo.
 * Utiliza o mapeamento de teclas definido na classe Config (via config.properties).
 * Atualiza o estado das ações (como movimentação) de acordo com as teclas pressionadas/liberadas.
 */
public class KeyHandler implements KeyListener {

    // Estados booleanos representando quais ações estão ativas
    public boolean upPressed, downPressed, leftPressed, rightPressed;

    /**
     * Chamado quando uma tecla é digitada (pressionada e liberada rapidamente).
     * Não utilizado neste projeto, mas faz parte da interface KeyListener.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // Ignorado no momento; útil para campos de texto ou menus em jogos futuros.
    }

    /**
     * Chamado quando uma tecla é pressionada.
     * Atualiza o estado das ações com base no código da tecla e no mapeamento de GameAction -> KeyCode.
     *
     * @param e Evento de tecla pressionada.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        Map<GameAction, Integer> keys = Config.KEY_BINDINGS;

        if (code == keys.get(GameAction.MOVE_UP))    upPressed = true;
        if (code == keys.get(GameAction.MOVE_DOWN))  downPressed = true;
        if (code == keys.get(GameAction.MOVE_LEFT))  leftPressed = true;
        if (code == keys.get(GameAction.MOVE_RIGHT)) rightPressed = true;
    }

    /**
     * Chamado quando uma tecla é liberada.
     * Atualiza o estado das ações para falso, interrompendo o comportamento correspondente.
     *
     * @param e Evento de tecla liberada.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        Map<GameAction, Integer> keys = Config.KEY_BINDINGS;

        if (code == keys.get(GameAction.MOVE_UP))    upPressed = false;
        if (code == keys.get(GameAction.MOVE_DOWN))  downPressed = false;
        if (code == keys.get(GameAction.MOVE_LEFT))  leftPressed = false;
        if (code == keys.get(GameAction.MOVE_RIGHT)) rightPressed = false;
    }
}
