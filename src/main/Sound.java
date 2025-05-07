package main;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import utils.SoundType;

/**
 * Classe responsável pela reprodução de sons do jogo.
 * Sons são carregados via enum {@link SoundType} com base em caminhos definidos
 * no `config.properties`.
 */
public class Sound {

    /** Mapa que associa cada tipo de som ao seu respectivo URL carregado. */
    private final Map<SoundType, URL> soundMap = new HashMap<>();

    /** Instância de áudio atualmente carregada. */
    private Clip clip;

    /**
     * Construtor que inicializa os caminhos de som a partir da configuração
     * externa.
     */
    public Sound() {
        for (SoundType type : SoundType.values()) {
            String path = Config.getProperty(type.getConfigKey());
            if (path != null) {
                URL soundUrl = getClass().getResource(path);
                if (soundUrl != null) {
                    soundMap.put(type, soundUrl);
                } else {
                    System.err.println("Som não encontrado no path: " + path);
                }
            } else {
                System.err.println("Chave de som não configurada: " + type.getConfigKey());
            }
        }
    }

    /**
     * Carrega um som a partir do enum {@link SoundType}.
     *
     * @param type Tipo de som a ser carregado.
     */
    public void setFile(SoundType type) {
        try {
            URL url = soundMap.get(type);
            if (url == null) {
                System.err.println("Som não carregado para o tipo: " + type);
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(ais);

        } catch (Exception e) {
            System.err.println("Erro ao carregar som [" + type + "]: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Inicia a reprodução do som atual. */
    public void play() {
        if (clip != null)
            clip.start();
    }

    /** Reproduz o som atual em loop contínuo. */
    public void loop() {
        if (clip != null)
            clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /** Para a reprodução do som atual. */
    public void stop() {
        if (clip != null)
            clip.stop();
    }
}
