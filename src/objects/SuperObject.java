package objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Config;
import main.GamePanel;

public class SuperObject {

    public BufferedImage image; // Imagem do objeto a ser desenhada no mundo
    public String name; // Nome identificador do objeto (ex: "Key", "Door", etc.)
    public boolean collision = false; // Indica se o objeto bloqueia movimento do jogador
    public int worldX, worldY; // Posição do objeto no mundo (em pixels)
    /**
     * Área sólida usada para colisão com o jogador ou outros objetos.
     * Os valores padrão definem uma hitbox quadrada centralizada no tile.
     * Dimensões e offsets podem ser ajustadas por objeto, conforme necessidade.
     */
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);

    /**
     * Posição padrão (offset) da hitbox para restaurar estado após modificações
     * temporárias.
     */
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    /**
     * Renderiza o objeto na tela, considerando o deslocamento da câmera em relação
     * ao jogador.
     *
     * @param g2 Contexto gráfico usado para desenhar
     * @param gp Referência ao painel principal do jogo
     */
    public void draw(Graphics2D g2, GamePanel gp) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Otimização: só desenha se estiver dentro da área visível
        if (worldX + Config.TILE_SIZE > gp.player.worldX - gp.player.screenX &&
                worldX - Config.TILE_SIZE < gp.player.worldX + gp.player.screenX &&
                worldY + Config.TILE_SIZE > gp.player.worldY - gp.player.screenY &&
                worldY - Config.TILE_SIZE < gp.player.worldY + gp.player.screenY) {

            g2.drawImage(image, screenX, screenY, Config.TILE_SIZE, Config.TILE_SIZE, null);
        }
    }
}
