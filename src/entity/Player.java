package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
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

    public final int screenX;
    public final int screenY;

    /**
     * Construtor do jogador.
     *
     * @param gp   Referência ao painel principal do jogo
     * @param keyH Manipulador de entrada do teclado
     */
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        // Posição fixa na tela onde o jogador será desenhado (centro da tela)
        // A câmera é centralizada no jogador, movendo o mundo ao redor dele
        screenX = Config.SCREEN_WIDTH / 2 - (Config.TILE_SIZE / 2);
        screenY = Config.SCREEN_HEIGHT / 2 - (Config.TILE_SIZE / 2);

        // ======================================================================
        // Inicializa a área sólida (hitbox) da entidade, usada para detecção de
        // colisão.
        // Por padrão, a hitbox é centralizada e menor que o tile para suavizar a
        // movimentação.
        //
        // O valor de "offset" é configurável via 'config.properties' e reduz as bordas
        // da área
        // de colisão em relação ao tamanho total do tile, evitando colisões injustas.
        // ======================================================================
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = Config.TILE_SIZE - Config.COLLISION_BOX_OFFSET;
        solidArea.height = Config.TILE_SIZE - Config.COLLISION_BOX_OFFSET;

        setDefaultValues();
        getPlayerImage();
    }

    /**
     * Define valores iniciais do jogador com base na configuração do jogo.
     */
    public void setDefaultValues() {
        this.worldX = Config.WORLD_INITIAL_X;
        this.worldY = Config.WORLD_INITIAL_Y;
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
            } else if (keyH.downPressed) {
                direction = Direction.DOWN;
            } else if (keyH.leftPressed) {
                direction = Direction.LEFT;
            } else if (keyH.rightPressed) {
                direction = Direction.RIGHT;
            }

            // Check Tile Collision
            collisionOn = false;
            gp.collisionChecker.checkTile(this);

            // IF Collision is false, playercan move
            if (collisionOn == false) {
                switch (direction) {
                    case UP:
                        worldY -= speed;
                        break;
                    case DOWN:
                        worldY += speed;
                        break;
                    case LEFT:
                        worldX -= speed;
                        break;
                    case RIGHT:
                        worldX += speed;
                        break;
                }
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

        g2.drawImage(image, screenX, screenY, Config.TILE_SIZE, Config.TILE_SIZE, null);
    }
}
