package tile;

import java.awt.image.BufferedImage;

/**
 * Representa uma unidade básica do mapa (tile), que pode conter uma imagem e
 * definir colisão.
 */
public class Tile {

    /** Imagem visual do tile usada durante a renderização do mapa */
    public BufferedImage image;

    /** Define se o tile possui colisão com o jogador ou NPCs */
    public boolean collision = false;
}
