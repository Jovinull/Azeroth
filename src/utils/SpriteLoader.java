package utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Carrega sprites a partir de um JSON leve com formato conhecido, usando apenas
 * Java puro.
 */
public class SpriteLoader {

    /**
     * Carrega os sprites de um personagem a partir de um JSON manualmente parseado.
     *
     * @param jsonPath caminho do JSON no classpath
     * @param basePath pasta base das imagens (ex: "/res/player/")
     * @return Mapa com direções (UP, DOWN, LEFT, RIGHT) e arrays de sprites
     *         carregados
     */
    public static Map<String, BufferedImage[]> loadSprites(String jsonPath, String basePath) {
        Map<String, BufferedImage[]> spriteMap = new HashMap<>();

        try {
            // Valida se o JSON existe
            InputStream is = SpriteLoader.class.getResourceAsStream(jsonPath);
            if (is == null) {
                throw new RuntimeException("Arquivo JSON não encontrado: " + jsonPath);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            String currentDirection = null;
            BufferedImage[] currentImages = new BufferedImage[2];
            int imageIndex = 0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Detecta início de chave
                if (line.startsWith("\"") && line.contains(":")) {
                    int quoteEnd = line.indexOf("\"", 1);
                    currentDirection = line.substring(1, quoteEnd).toUpperCase();
                    imageIndex = 0;
                } else if (line.contains(".png")) {
                    // Extrai o nome do arquivo
                    int start = line.indexOf("\"") + 1;
                    int end = line.lastIndexOf("\"");
                    String fileName = line.substring(start, end);

                    // Valida se a imagem existe
                    InputStream imgStream = SpriteLoader.class.getResourceAsStream(basePath + fileName);
                    if (imgStream == null) {
                        throw new RuntimeException("Imagem não encontrada: " + basePath + fileName);
                    }

                    BufferedImage img = ImageIO.read(imgStream);
                    currentImages[imageIndex++] = img;

                    // Quando tiver 2 imagens, armazena no mapa
                    if (imageIndex == 2 && currentDirection != null) {
                        spriteMap.put(currentDirection, currentImages);
                        currentImages = new BufferedImage[2]; // reseta array
                    }
                }
            }

            reader.close();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar sprites: " + e.getMessage(), e);
        }

        return spriteMap;
    }
}
