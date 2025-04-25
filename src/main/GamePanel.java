package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import utils.FpsMonitor;

/**
 * GamePanel representa o componente gráfico principal onde o jogo é
 * renderizado.
 * Contém o loop principal de atualização/desenho e manipula o estado do jogador
 * com base nas entradas.
 */
public class GamePanel extends JPanel implements Runnable {

    // Manipulador de teclas, escutando eventos definidos via configuração
    private final KeyHandler keyH = new KeyHandler();

    // Thread dedicada ao loop principal do jogo (uso de volatile para garantir
    // visibilidade entre threads)
    private volatile Thread gameThread;

    // ==========================
    // Estado do jogador
    // ==========================

    // Posição inicial do jogador
    private int playerX = Config.PLAYER_INITIAL_X;
    private int playerY = Config.PLAYER_INITIAL_Y;

    // Velocidade de movimento do jogador
    private final int playerSpeed = Config.PLAYER_SPEED;

    // ==========================
    // Controle de FPS
    // ==========================

    // Frames por segundo desejados
    private final int FPS = Config.FPS;

    // Monitoramento de FPS para debug (inicialmente desativado)
    private final FpsMonitor fpsMonitor = new FpsMonitor(Config.ENABLE_FPS_MONITOR);

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

                fpsMonitor.frameRendered(); // Registra que um frame foi renderizado (debug opcional)
            }
        }
    }

    /**
     * Atualiza a posição do jogador com base nos estados das teclas.
     * Permite movimentação contínua enquanto a tecla estiver pressionada.
     * Agora permite movimentações diagonais ao detectar múltiplas teclas
     * pressionadas simultaneamente.
     */
    public void update() {
        if (keyH.upPressed) {
            playerY -= playerSpeed;
        }
        if (keyH.downPressed) {
            playerY += playerSpeed;
        }
        if (keyH.leftPressed) {
            playerX -= playerSpeed;
        }
        if (keyH.rightPressed) {
            playerX += playerSpeed;
        }
    }

    /**
     * Responsável por desenhar os elementos gráficos do jogo na tela.
     *
     * @param g Contexto gráfico fornecido pelo sistema Swing.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // limpa o painel antes de desenhar

        Graphics2D g2 = (Graphics2D) g;

        drawPlayer(g2); // Isola a responsabilidade de desenhar o jogador

        g2.dispose(); // Libera recursos gráficos
    }

    /**
     * Método separado para desenhar o jogador.
     * Essa separação melhora a organização do código e facilita futuras adições de
     * novos elementos gráficos.
     *
     * @param g2 contexto gráfico 2D
     */
    private void drawPlayer(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillRect(playerX, playerY, Config.TILE_SIZE, Config.TILE_SIZE);
    }

    /**
     * Permite ativar ou desativar o monitoramento de FPS dinamicamente.
     * Útil para ambientes de desenvolvimento e depuração.
     *
     * @param enabled true para ativar a exibição de FPS, false para desativar
     */
    public void setFpsMonitorEnabled(boolean enabled) {
        fpsMonitor.setEnabled(enabled);
    }
}
