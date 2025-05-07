package utils;

/**
 * Enum que representa os tipos de sons disponíveis no jogo.
 * Cada constante se associa a uma chave de configuração no `config.properties`.
 */
public enum SoundType {
    BLUEBOY_THEME("sound.theme"),
    COIN("sound.coin"),
    POWER_UP("sound.powerup"),
    UNLOCK("sound.unlock"),
    FANFARE("sound.fanfare");

    private final String configKey;

    SoundType(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigKey() {
        return configKey;
    }
}
