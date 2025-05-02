package entity;

import java.awt.image.BufferedImage;

/**
 * Classe base para todas as entidades do jogo que possuem sprite e direção.
 * Serve como superclasse para jogadores, NPCs, inimigos, etc.
 */
public class Entity {
    public int x, y; // Coordenadas da entidade na tela
    public int speed; // Velocidade da entidade

    // Sprites da entidade para animação em cada direção
    public BufferedImage up1, up2;
    public BufferedImage down1, down2;
    public BufferedImage left1, left2;
    public BufferedImage right1, right2;

    public Direction direction; // Direção atual da entidade

    public int spriteCounter = 0; // Contador usado para alternar os sprites
    public int spriteNumber = 1; // Alternância entre sprite 1 e 2
}
