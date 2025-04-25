package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * GamePanel representa o componente gráfico principal onde o jogo é
 * renderizado.
 * Contém o loop principal de atualização/desenho e manipula o estado do jogador
 * com base nas entradas.
 */
public class GamePanel extends JPanel implements Runnable {

    // Manipulador de teclas, escutando eventos definidos via configuração
    KeyHandler keyH = new KeyHandler();

    // Thread dedicada ao loop principal do jogo
    Thread gameThread;

    // ==========================
    // Estado do jogador
    // ==========================

    // Posição inicial do jogador
    int playerX = Config.PLAYER_INITIAL_X;
    int playerY = Config.PLAYER_INITIAL_Y;

    // Velocidade de movimento do jogador
    int playerSpeed = Config.PLAYER_SPEED;

    // ==========================
    // Controle de FPS
    // ==========================

    // Frames por segundo desejados
    int FPS = Config.FPS;

    /**
     * Construtor do painel do jogo.
     * Define o tamanho, a cor de fundo e inicializa escuta de teclado.
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true); // Minimiza flickering na renderização
        this.addKeyListener(keyH); // Permite detectar entradas do jogador
        this.setFocusable(true); // Garante que o painel pode receber foco do teclado
    }

    /**
     * Inicia a execução da thread de jogo com base no método run().
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Loop principal do jogo.
     * Controla a taxa de atualização baseada no tempo e garante consistência de
     * FPS.
     */
    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS; // nanosegundos por frame
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update(); // Atualiza o estado do jogo (ex: movimentação)
                repaint(); // Redesenha os elementos na tela
                delta--;
            }
        }
    }

    /**
     * Atualiza a posição do jogador com base nos estados das teclas.
     * Permite movimentação contínua enquanto a tecla estiver pressionada.
     */
    public void update() {
        if (keyH.upPressed) {
            playerY -= playerSpeed;
        } else if (keyH.downPressed) {
            playerY += playerSpeed;
        } else if (keyH.leftPressed) {
            playerX -= playerSpeed;
        } else if (keyH.rightPressed) {
            playerX += playerSpeed;
        }
    }

    /**
     * Responsável por desenhar os elementos gráficos do jogo na tela.
     * No momento, desenha um retângulo representando o jogador.
     *
     * @param g Contexto gráfico fornecido pelo sistema Swing.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // limpa o painel antes de desenhar

        Graphics2D g2 = (Graphics2D) g;

        // Cor do jogador
        g2.setColor(Color.WHITE);

        // Desenha o jogador como um quadrado baseado no tileSize
        g2.fillRect(playerX, playerY, Config.TILE_SIZE, Config.TILE_SIZE);

        g2.dispose(); // Libera recursos gráficos
    }
}
