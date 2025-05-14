package main;

/**
 * Interface para controle do loop principal de execução do jogo.
 */
public interface GameLoopController {

    /**
     * Inicia a thread principal do jogo.
     */
    void startGameThread();

    /**
     * Para a thread principal do jogo.
     */
    void stopGameThread();

    /**
     * Verifica se a thread principal do jogo está em execução.
     *
     * @return true se ativa, false se parada
     */
    boolean isGameRunning();
}
