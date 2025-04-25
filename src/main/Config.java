package main;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

/**
 * Classe de configuração dinâmica do jogo.
 * Carrega e valida os parâmetros de tela, controles, FPS e jogador a partir do
 * arquivo externo 'config.properties'.
 * Garante integridade dos dados e evita execução com valores inválidos.
 */
public class Config {

    // ================
    // Configurações base carregadas do arquivo
    // ================
    public static int ORIGINAL_TILE_SIZE;
    public static int SCALE;
    public static int MAX_SCREEN_COL;
    public static int MAX_SCREEN_ROW;
    public static String WINDOW_TITLE;

    // ================
    // Configurações derivadas (calculadas com base nos valores acima)
    // ================
    public static int TILE_SIZE;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    // ================
    // Configurações do jogador e performance
    // ================
    public static int PLAYER_INITIAL_X;
    public static int PLAYER_INITIAL_Y;
    public static int PLAYER_SPEED;
    public static int FPS;

    // ================
    // Mapeamento de teclas para ações do jogo
    // ================
    public static final Map<GameAction, Integer> KEY_BINDINGS = new EnumMap<>(GameAction.class);

    // Bloco estático executado ao carregar a classe
    static {
        load();
    }

    /**
     * Carrega e valida todas as configurações do arquivo 'config.properties'.
     * Em caso de erro, exibe uma mensagem e encerra a execução.
     */
    private static void load() {
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);

            // Parâmetros visuais
            ORIGINAL_TILE_SIZE = parsePositiveInt(props, "originalTileSize");
            SCALE = parsePositiveInt(props, "scale");
            MAX_SCREEN_COL = parsePositiveInt(props, "maxScreenCol");
            MAX_SCREEN_ROW = parsePositiveInt(props, "maxScreenRow");
            WINDOW_TITLE = props.getProperty("windowTitle", "Jogo").trim();

            // Cálculo de dimensões derivadas
            TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
            SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
            SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;

            // Configurações do jogador
            PLAYER_INITIAL_X = parsePositiveInt(props, "playerInitialX");
            PLAYER_INITIAL_Y = parsePositiveInt(props, "playerInitialY");
            PLAYER_SPEED = parsePositiveInt(props, "playerSpeed");

            // Configuração de FPS
            FPS = parsePositiveInt(props, "fps");

            // Mapeamento de teclas
            loadKeyBindings(props);

        } catch (IOException e) {
            System.err.println(
                    "Erro ao carregar o arquivo 'config.properties'. Verifique se ele existe e está acessível.");
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
     * @param props Objeto Properties carregado do arquivo
     * @param key   Nome da propriedade a ser validada
     * @return Valor inteiro positivo da propriedade
     * @throws IllegalArgumentException se a propriedade for ausente, inválida ou <=
     *                                  0
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

    /**
     * Carrega e associa teclas do config.properties a ações do enum GameAction.
     */
    private static void loadKeyBindings(Properties props) {
        bindKey(props, GameAction.MOVE_UP, "key.moveUp");
        bindKey(props, GameAction.MOVE_DOWN, "key.moveDown");
        bindKey(props, GameAction.MOVE_LEFT, "key.moveLeft");
        bindKey(props, GameAction.MOVE_RIGHT, "key.moveRight");
    }

    /**
     * Associa uma tecla (configurada como string) a uma ação do jogo.
     */
    private static void bindKey(Properties props, GameAction action, String propertyKey) {
        String keyName = props.getProperty(propertyKey);
        if (keyName == null || keyName.isBlank()) {
            throw new IllegalArgumentException("A tecla para a ação '" + action + "' não foi definida.");
        }

        int keyCode = parseKeyCode(keyName.trim().toUpperCase());
        KEY_BINDINGS.put(action, keyCode);
    }

    /**
     * Converte uma string de tecla para o código KeyEvent correspondente.
     */
    private static int parseKeyCode(String keyString) {
        if (keyString.length() == 1) {
            return KeyEvent.getExtendedKeyCodeForChar(keyString.charAt(0));
        }

        try {
            return KeyEvent.class.getField("VK_" + keyString).getInt(null);
        } catch (Exception e) {
            throw new IllegalArgumentException("Tecla inválida: " + keyString);
        }
    }
}
