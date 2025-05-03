package utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import tile.Tile;

/**
 * TileLoader é responsável por carregar os tiles do mundo a partir de um
 * arquivo JSON.
 * Cada tile possui uma imagem e uma configuração opcional de colisão.
 */
public class TileLoader {

    /**
     * Carrega os tiles definidos no JSON e retorna um mapa de índice para Tile.
     *
     * @param jsonPath caminho do JSON (classpath)
     * @param basePath pasta onde estão os arquivos de imagem dos tiles
     * @return Mapa de tiles com índice inteiro como chave
     */
    public static Map<Integer, Tile> loadTiles(String jsonPath, String basePath) {
        Map<Integer, Tile> tileMap = new HashMap<>();

        try {
            InputStream is = TileLoader.class.getResourceAsStream(jsonPath);
            if (is == null) {
                throw new RuntimeException("Arquivo de tiles JSON não encontrado: " + jsonPath);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            Integer currentId = null;
            Tile currentTile = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.matches("^\"\\d+\"\\s*:\\s*\\{")) {
                    int id = Integer.parseInt(line.substring(1, line.indexOf("\"", 1)));
                    currentTile = new Tile();
                    currentId = id;
                } else if (line.contains("\"file\"")) {
                    String fileName = extractValue(line);
                    InputStream imgStream = TileLoader.class.getResourceAsStream(basePath + fileName);
                    if (imgStream == null) {
                        throw new RuntimeException("Imagem de tile não encontrada: " + basePath + fileName);
                    }
                    BufferedImage img = ImageIO.read(imgStream);
                    currentTile.image = img;
                } else if (line.contains("\"collision\"")) {
                    String collisionVal = extractValue(line);
                    currentTile.collision = Boolean.parseBoolean(collisionVal);
                } else if (line.contains("}")) {
                    tileMap.put(currentId, currentTile);
                }
            }

            reader.close();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar tiles: " + e.getMessage(), e);
        }

        return tileMap;
    }

    /**
     * Extrai valor de string JSON no formato: "chave": valor
     */
    private static String extractValue(String line) {
        int start = line.indexOf(":") + 1;
        String value = line.substring(start).replace("\"", "").replace(",", "").trim();
        return value;
    }
}
