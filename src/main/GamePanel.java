package main;

import javax.swing.*;
import java.awt.*;

/**
 * GamePanel representa o componente principal onde o jogo será renderizado.
 */
public class GamePanel extends JPanel implements Runnable {

    // Thread dedicada ao loop do jogo
    Thread gameThread;

    /**
     * Construtor da classe GamePanel.
     * Define as propriedades visuais e estruturais do painel de jogo.
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true); // evita flickering
    }

    /**
     * Inicia a thread de execução do loop principal do jogo.
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        // TODO: Implementar loop do jogo aqui
    }
}
