package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Classe de configuração dinâmica do jogo.
 * Carrega e valida os parâmetros de tela definidos no arquivo externo 'config.properties'.
 * Garante integridade dos dados e evita execução com valores inválidos.
 */
public class Config {

    // ================
    // Configurações base carregadas do arquivo
    // ================
    public static int ORIGINAL_TILE_SIZE; // Tamanho base do tile, sem escala
    public static int SCALE;              // Fator de escala aplicado ao tile
    public static int MAX_SCREEN_COL;     // Número de colunas visíveis na tela
    public static int MAX_SCREEN_ROW;     // Número de linhas visíveis na tela

    // ================
    // Configurações derivadas (calculadas com base nos valores acima)
    // ================
    public static int TILE_SIZE;      // Tamanho final do tile (tile base x escala)
    public static int SCREEN_WIDTH;   // Largura total da tela (em pixels)
    public static int SCREEN_HEIGHT;  // Altura total da tela (em pixels)

    // Bloco estático executado ao carregar a classe
    static {
        load();
    }

    /**
     * Carrega e valida as configurações do arquivo 'config.properties'.
     * Também calcula os valores derivados com base nos parâmetros principais.
     * Em caso de erro, exibe mensagem descritiva e encerra o programa.
     */
    private static void load() {
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);

            // Carregamento e validação dos parâmetros obrigatórios
            ORIGINAL_TILE_SIZE = parsePositiveInt(props, "originalTileSize");
            SCALE = parsePositiveInt(props, "scale");
            MAX_SCREEN_COL = parsePositiveInt(props, "maxScreenCol");
            MAX_SCREEN_ROW = parsePositiveInt(props, "maxScreenRow");

            // Cálculo das dimensões derivadas
            TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
            SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
            SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;

        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo 'config.properties'. Verifique se ele existe e está acessível.");
            e.printStackTrace();
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação na configuração: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Lê e valida uma propriedade obrigatória do tipo inteiro positivo.
     *
     * @param props  Objeto Properties carregado do arquivo
     * @param key    Nome da propriedade a ser validada
     * @return       Valor inteiro positivo da propriedade
     * @throws IllegalArgumentException se a propriedade for ausente, inválida ou <= 0
     */
    private static int parsePositiveInt(Properties props, String key) {
        String value = props.getProperty(key);

        // Verifica se a propriedade está presente
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("A propriedade obrigatória '" + key + "' está ausente.");
        }

        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed <= 0) {
                throw new IllegalArgumentException("O valor de '" + key + "' deve ser maior que zero.");
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("O valor de '" + key + "' deve ser um número inteiro válido.");
        }
    }
}
