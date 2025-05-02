package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

import main.Config;
import main.GamePanel;
import main.KeyHandler;
import utils.SpriteLoader;

/**
 * Classe responsável por representar o jogador no jogo.
 * Controla sua posição, movimentação, direção e animações com base nas entradas
 * do teclado.
 */
public class Player extends Entity {
    private GamePanel gp;
    private KeyHandler keyH;

    // ==========================
    // Estado do jogador
    // ==========================

    private int x; // Coordenada X atual do jogador
    private int y; // Coordenada Y atual do jogador
    private int speed; // Velocidade de movimento em pixels por atualização

    /**
     * Construtor do jogador.
     *
     * @param gp   Referência ao painel principal do jogo
     * @param keyH Manipulador de entrada do teclado
     */
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
        getPlayerImage();
    }

    /**
     * Define valores iniciais do jogador com base na configuração do jogo.
     */
    public void setDefaultValues() {
        this.x = Config.PLAYER_INITIAL_X;
        this.y = Config.PLAYER_INITIAL_Y;
        this.speed = Config.PLAYER_SPEED;
        direction = Direction.DOWN;
    }

    /**
     * Carrega os sprites do jogador a partir dos recursos gráficos.
     */
    public void getPlayerImage() {
        Map<String, BufferedImage[]> sprites = SpriteLoader.loadSprites(
                "/res/player/player_sprites.json", "/res/player/");

        up1 = sprites.get("UP")[0];
        up2 = sprites.get("UP")[1];
        down1 = sprites.get("DOWN")[0];
        down2 = sprites.get("DOWN")[1];
        left1 = sprites.get("LEFT")[0];
        left2 = sprites.get("LEFT")[1];
        right1 = sprites.get("RIGHT")[0];
        right2 = sprites.get("RIGHT")[1];
    }

    /**
     * Atualiza a posição e animação do jogador com base nas teclas pressionadas.
     */
    public void update() {
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                direction = Direction.UP;
                y -= speed;
            } else if (keyH.downPressed) {
                direction = Direction.DOWN;
                y += speed;
            } else if (keyH.leftPressed) {
                direction = Direction.LEFT;
                x -= speed;
            } else if (keyH.rightPressed) {
                direction = Direction.RIGHT;
                x += speed;
            }

            spriteCounter++;
            // 60 FPS: trocar sprite a cada 10 frames → 6 mudanças por segundo
            if (spriteCounter > 10) {
                spriteNumber = (spriteNumber == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    /**
     * Responsável por desenhar o jogador na tela.
     *
     * @param g2 Contexto gráfico 2D fornecido pelo GamePanel
     */
    public void draw(Graphics2D g2) {
        drawPlayer(g2);
    }

    /**
     * Lógica de desenho com base na direção e sprite atual.
     *
     * @param g2 Contexto gráfico onde o jogador será desenhado
     */
    private void drawPlayer(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case UP:
                image = (spriteNumber == 1) ? up1 : up2;
                break;
            case DOWN:
                image = (spriteNumber == 1) ? down1 : down2;
                break;
            case LEFT:
                image = (spriteNumber == 1) ? left1 : left2;
                break;
            case RIGHT:
                image = (spriteNumber == 1) ? right1 : right2;
                break;
            default:
                break;
        }

        g2.drawImage(image, x, y, Config.TILE_SIZE, Config.TILE_SIZE, null);
    }
}
