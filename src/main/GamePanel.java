package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import objects.SuperObject;
import tile.TileManager;
import utils.AssetSetter;
import utils.CollisionChecker;
import utils.FpsMonitor;
import utils.SoundType;

/**
 * GamePanel representa o componente gráfico principal onde o jogo é
 * renderizado.
 * Contém o loop principal de atualização/desenho e manipula o estado do jogador
 * com base nas entradas.
 */
public class GamePanel extends JPanel implements Runnable, GameLoopController {

    private TileManager tileManager = new TileManager(this); // Gerenciador responsável por carregar e desenhar o mapa

    // Manipulador de teclas, escutando eventos definidos via configuração
    private final KeyHandler keyH = new KeyHandler();

    public GameState gameState = new GameState();

    public CollisionChecker collisionChecker = new CollisionChecker(this);

    Sound music = new Sound();
    Sound se = new Sound();

    public AssetSetter aSetter = new AssetSetter(this);

    public UI ui = new UI(this);

    // Instancia o jogador, passando o painel atual e o manipulador de teclas
    public Player player = new Player(this, keyH);

    // Array que armazena todos os objetos ativos no mapa.
    public SuperObject obj[] = new SuperObject[10];

    // World Settings
    public static final int MAX_WORLD_COL = 50;
    public static final int MAX_WORLD_ROW = 50;

    // Thread dedicada ao loop principal do jogo (uso de volatile para garantir
    // visibilidade entre threads)
    private volatile Thread gameThread;

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
     * Método responsável pela configuração inicial do jogo.
     * Neste estágio, realiza o posicionamento dos objetos no mapa por meio do
     * AssetSetter.
     * Ideal para futuras expansões como carregamento de NPCs, inimigos ou itens.
     */
    public void setupGame() {
        aSetter.setObject(); // Posiciona os objetos no mundo com base em posições predefinidas
        playMusic(SoundType.BLUEBOY_THEME);
    }

    /**
     * Inicia a thread principal do jogo, caso ela ainda não esteja em execução.
     * Garante que múltiplas instâncias não sejam criadas simultaneamente.
     */
    public void startGameThread() {
        if (gameThread == null || !gameThread.isAlive()) {
            gameThread = new Thread(this); // 'this' deve implementar Runnable
            gameThread.start();
        }
    }

    /**
     * Interrompe a thread principal do jogo de forma controlada.
     * Utiliza o método interrupt() como sinalização de parada.
     */
    public void stopGameThread() {
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt(); // Sugere-se que o loop principal respeite esse sinal
        }
    }

    /**
     * Verifica se a thread principal do jogo está ativa e em execução.
     *
     * @return true se a thread estiver viva; false caso contrário
     */
    public boolean isGameRunning() {
        return gameThread != null && gameThread.isAlive();
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
     * Método responsável por desenhar todos os elementos gráficos da tela.
     * Executa a renderização na ordem correta: mapa (tiles), objetos, jogador.
     *
     * @param g Contexto gráfico fornecido pelo Swing.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // limpa o painel antes de desenhar

        Graphics2D g2 = (Graphics2D) g;

        // TILE
        tileManager.draw(g2); // Solicita ao TileManager que desenhe o mapa antes do jogador

        // OBJECT
        // Percorre o array de objetos e desenha apenas os não-nulos
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }

        // PLAYER
        // Desenha o jogador na tela utilizando o contexto gráfico 2D
        player.draw(g2);

        // UI
        ui.draw(g2);

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

    /**
     * Fornece acesso controlado ao gerenciador de tiles.
     *
     * @return instância de TileManager
     */
    public TileManager getTileManager() {
        return tileManager;
    }

    /**
     * Reproduz uma música de fundo definida pelo tipo {@link SoundType}.
     * A música é configurada para loop contínuo.
     *
     * @param type Tipo de música a ser reproduzida
     */
    public void playMusic(SoundType type) {
        music.setFile(type);
        music.play();
        music.loop();
    }

    /**
     * Para a reprodução da música atual.
     */
    public void stopMusic() {
        music.stop();
    }

    /**
     * Reproduz um efeito sonoro (Sound Effect) uma única vez.
     *
     * @param type Tipo de efeito sonoro a ser reproduzido
     */
    public void playSE(SoundType type) {
        se.setFile(type);
        se.play();
    }
}
