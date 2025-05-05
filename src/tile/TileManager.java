package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import main.Config;
import main.GamePanel;
import utils.TileLoader;

/**
 * Gerencia o carregamento e a renderização dos tiles do mapa.
 * Lê as imagens dos tiles e interpreta o layout do mapa a partir de um arquivo
 * texto.
 */
public class TileManager {

    GamePanel gp; // Referência ao painel principal do jogo, necessária para acesso às dimensões e
                  // contexto

    private Map<Integer, Tile> tileMap; // Vetor de tipos de tiles possíveis (grama, parede, água, etc.)
    private int[][] mapTileNum; // Mapa bidimensional indicando qual tile está presente em cada posição

    /**
     * Construtor do gerenciador de tiles.
     * Inicializa os recursos gráficos e carrega o mapa a partir de um arquivo.
     *
     * @param gp Instância do GamePanel principal, para acesso às configurações do
     *           jogo.
     */
    public TileManager(GamePanel gp) {
        this.gp = gp;

        this.mapTileNum = new int[GamePanel.MAX_WORLD_COL][GamePanel.MAX_WORLD_ROW];
        this.tileMap = TileLoader.loadTiles(Config.TILE_CONFIG_PATH, Config.TILE_IMAGE_BASE);
        loadMap(Config.MAP_DEFAULT_PATH); // Carrega o layout do mapa
    }

    /**
     * Carrega o layout do mapa a partir de um arquivo texto.
     * Cada linha do arquivo representa uma linha da grade de tiles.
     * Cada número separado por espaço representa o índice de um tile no array
     * `tile[]`.
     *
     * @param filePath Caminho relativo do arquivo de mapa dentro do classpath (ex:
     *                 "/res/maps/map01.txt").
     */
    public void loadMap(String filePath) {
        try {
            // Abre o arquivo como stream, a partir do caminho informado
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            // Lê linha por linha até preencher toda a grade definida por MAX_SCREEN_COL e
            // MAX_SCREEN_ROW
            while (col < GamePanel.MAX_WORLD_COL && row < GamePanel.MAX_WORLD_ROW) {
                String line = br.readLine(); // Lê uma linha do mapa

                while (col < GamePanel.MAX_WORLD_COL) {
                    // Divide a linha em números (tiles) separados por espaço
                    String[] numbers = line.split(" ");

                    // Converte o número textual para inteiro, que representa o tipo de tile
                    int number = Integer.parseInt(numbers[col]);

                    // Armazena no mapa a posição correspondente ao tile
                    mapTileNum[col][row] = number;
                    col++;
                }

                // Quando termina de ler uma linha inteira, passa para a próxima
                if (col == GamePanel.MAX_WORLD_COL) {
                    col = 0;
                    row++;
                }
            }

            br.close(); // Libera o recurso após leitura completa

        } catch (Exception e) {
            // Em produção, substituir por sistema de logging (ex: SLF4J, Log4j) para maior
            // controle
            e.printStackTrace();
        }
    }

    /**
     * Renderiza o mapa completo de tiles na tela.
     *
     * @param g2 contexto gráfico para desenhar
     */
    /**
     * Renderiza todos os tiles na tela com base na matriz do mapa carregado.
     *
     * @param g2 Contexto gráfico usado para desenhar as imagens dos tiles.
     */
    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        // Itera por todas as colunas e linhas da matriz de mapa
        while (worldCol < GamePanel.MAX_WORLD_COL && worldRow < GamePanel.MAX_WORLD_ROW) {

            int tileNum = mapTileNum[worldCol][worldRow]; // Obtém o tipo de tile naquela posição

            int worldX = worldCol * Config.TILE_SIZE;
            int worldY = worldRow * Config.TILE_SIZE;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            // Desenha o tile na posição atual
            if (worldX + Config.TILE_SIZE > gp.player.worldX - gp.player.screenX &&
                    worldX - Config.TILE_SIZE < gp.player.worldX + gp.player.screenX &&
                    worldY + Config.TILE_SIZE > gp.player.worldY - gp.player.screenY &&
                    worldY - Config.TILE_SIZE < gp.player.worldY + gp.player.screenY) {
                BufferedImage img = tileMap.get(tileNum).image;
                g2.drawImage(img, screenX, screenY, Config.TILE_SIZE, Config.TILE_SIZE, null);
            }

            worldCol++;

            // Avança para a próxima linha da grade quando uma linha estiver completa
            if (worldCol == GamePanel.MAX_WORLD_COL) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    /**
     * Retorna o número do tile na posição especificada.
     *
     * @param col coluna no mapa
     * @param row linha no mapa
     * @return índice do tile
     */
    public int getTileNumber(int col, int row) {
        return mapTileNum[col][row];
    }

    /**
     * Verifica se o tile especificado possui colisão.
     *
     * @param tileNum índice do tile
     * @return true se tiver colisão, false caso contrário
     */
    public boolean hasCollision(int tileNum) {
        Tile tile = tileMap.get(tileNum);
        return tile != null && tile.collision;
    }
}
