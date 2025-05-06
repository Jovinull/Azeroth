package utils;

import main.Config;
import main.GamePanel;
import objects.OBJ_Chest;
import objects.OBJ_Door;
import objects.OBJ_Key;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Define os objetos do mapa com base em posições fixas (temporárias).
     * Sugestão: externalizar essas posições no futuro para um arquivo JSON/CSV ou
     * config.properties.
     */
    public void setObject() {
        // Chaves
        gp.obj[0] = new OBJ_Key();
        gp.obj[0].worldX = 23 * Config.TILE_SIZE;
        gp.obj[0].worldY = 7 * Config.TILE_SIZE;

        gp.obj[1] = new OBJ_Key();
        gp.obj[1].worldX = 23 * Config.TILE_SIZE;
        gp.obj[1].worldY = 40 * Config.TILE_SIZE;

        gp.obj[2] = new OBJ_Key();
        gp.obj[2].worldX = 38 * Config.TILE_SIZE;
        gp.obj[2].worldY = 8 * Config.TILE_SIZE;

        // Portas
        gp.obj[3] = new OBJ_Door();
        gp.obj[3].worldX = 10 * Config.TILE_SIZE;
        gp.obj[3].worldY = 11 * Config.TILE_SIZE;

        gp.obj[4] = new OBJ_Door();
        gp.obj[4].worldX = 8 * Config.TILE_SIZE;
        gp.obj[4].worldY = 28 * Config.TILE_SIZE;

        gp.obj[5] = new OBJ_Door();
        gp.obj[5].worldX = 12 * Config.TILE_SIZE;
        gp.obj[5].worldY = 22 * Config.TILE_SIZE;

        // Baú
        gp.obj[6] = new OBJ_Chest();
        gp.obj[6].worldX = 10 * Config.TILE_SIZE;
        gp.obj[6].worldY = 7 * Config.TILE_SIZE;
    }
}
