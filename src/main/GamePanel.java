package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import tile.TileManager;
import utils.FpsMonitor;

/**
 * GamePanel representa o componente gráfico principal onde o jogo é
 * renderizado.
 * Contém o loop principal de atualização/desenho e manipula o estado do jogador
 * com base nas entradas.
 */
public class GamePanel extends JPanel implements Runnable {

    TileManager tileManager = new TileManager(this); // Gerenciador responsável por carregar e desenhar o mapa

    // Manipulador de teclas, escutando eventos definidos via configuração
    private final KeyHandler keyH = new KeyHandler();

    // Thread dedicada ao loop principal do jogo (uso de volatile para garantir
    // visibilidade entre threads)
    private volatile Thread gameThread;

    // Instancia o jogador, passando o painel atual e o manipulador de teclas
    Player player = new Player(this, keyH);

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
     * Atualiza o estado do jogo.
     * Neste momento, apenas o jogador é atualizado, mas futuros elementos (NPCs,
     * inimigos etc.)
     * também devem ser chamados aqui.
     */
    public void update() {
        player.update();
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

        tileManager.draw(g2); // Solicita ao TileManager que desenhe o mapa antes do jogador

        // Desenha o jogador na tela utilizando o contexto gráfico 2D
        player.draw(g2);

        g2.dispose(); // Libera recursos gráficos
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
