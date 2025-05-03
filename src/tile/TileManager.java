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

    Map<Integer, Tile> tileMap; // Vetor de tipos de tiles possíveis (grama, parede, água, etc.)
    int mapTileNum[][]; // Mapa bidimensional indicando qual tile está presente em cada posição

    /**
     * Construtor do gerenciador de tiles.
     * Inicializa os recursos gráficos e carrega o mapa a partir de um arquivo.
     *
     * @param gp Instância do GamePanel principal, para acesso às configurações do
     *           jogo.
     */
    public TileManager(GamePanel gp) {
        this.gp = gp;

        this.mapTileNum = new int[Config.MAX_SCREEN_COL][Config.MAX_SCREEN_ROW];
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
            while (col < Config.MAX_SCREEN_COL && row < Config.MAX_SCREEN_ROW) {
                String line = br.readLine(); // Lê uma linha do mapa

                while (col < Config.MAX_SCREEN_COL) {
                    // Divide a linha em números (tiles) separados por espaço
                    String[] numbers = line.split(" ");

                    // Converte o número textual para inteiro, que representa o tipo de tile
                    int number = Integer.parseInt(numbers[col]);

                    // Armazena no mapa a posição correspondente ao tile
                    mapTileNum[col][row] = number;
                    col++;
                }

                // Quando termina de ler uma linha inteira, passa para a próxima
                if (col == Config.MAX_SCREEN_COL) {
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
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        // Itera por todas as colunas e linhas da matriz de mapa
        while (col < Config.MAX_SCREEN_COL && row < Config.MAX_SCREEN_ROW) {

            int tileNum = mapTileNum[col][row]; // Obtém o tipo de tile naquela posição

            // Desenha o tile na posição atual
            BufferedImage img = tileMap.get(tileNum).image;
            g2.drawImage(img, x, y, Config.TILE_SIZE, Config.TILE_SIZE, null);

            col++;
            x += Config.TILE_SIZE;

            // Avança para a próxima linha da grade quando uma linha estiver completa
            if (col == Config.MAX_SCREEN_COL) {
                col = 0;
                x = 0;
                row++;
                y += Config.TILE_SIZE;
            }
        }
    }
}
