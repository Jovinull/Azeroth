package utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * Classe utilitária para carregamento seguro de imagens.
 */
public class ObjectImageUtils {

    /**
     * Carrega uma imagem do caminho fornecido dentro dos recursos do projeto.
     *
     * @param path Caminho relativo ao diretório de recursos (ex:
     *             "/res/objects/key.png")
     * @return A imagem carregada ou null em caso de falha (log já emitido)
     */
    public static BufferedImage loadImage(String path) {
        try (InputStream is = ObjectImageUtils.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Recurso de imagem não encontrado: " + path);
                return null;
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            System.err.println("Erro ao carregar imagem: " + path);
            e.printStackTrace();
            return null;
        }
    }
}
