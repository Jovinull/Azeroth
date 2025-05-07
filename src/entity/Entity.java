package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import utils.Direction;

/**
 * Classe base para todas as entidades do jogo que possuem sprite e direção.
 * Serve como superclasse para jogadores, NPCs, inimigos, etc.
 */
public class Entity {
    public int worldX, worldY; // Coordenadas da entidade no mundo, usadas para cálculo de posição relativa ao
                               // mapa
    public int speed; // Velocidade da entidade

    // Sprites da entidade para animação em cada direção
    public BufferedImage up1, up2;
    public BufferedImage down1, down2;
    public BufferedImage left1, left2;
    public BufferedImage right1, right2;

    public Direction direction; // Direção atual da entidade

    public int spriteCounter = 0; // Contador usado para alternar os sprites
    public int spriteNumber = 1; // Alternância entre sprite 1 e 2

    /**
     * Posição padrão (offset) da área sólida da entidade em relação à sua posição
     * (worldX, worldY).
     * Usado para resetar a área de colisão ao estado original após alterações
     * temporárias.
     */
    public int solidAreaDefaultX, solidAreaDefaultY;
    public Rectangle solidArea;
    public boolean collisionOn = false;

    /**
     * Restaura a posição da área sólida (hitbox) da entidade para os valores
     * padrão.
     * Deve ser chamado após simulações de movimento para evitar acúmulo de
     * deslocamentos.
     */
    public void resetSolidArea() {
        this.solidArea.x = this.solidAreaDefaultX;
        this.solidArea.y = this.solidAreaDefaultY;
    }
}
