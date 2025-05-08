package main;

/**
 * Gerencia estados globais do jogo, como tempo de jogo acumulado.
 */
public class GameState {
    private double playTime;

    /**
     * Atualiza o tempo com base no deltaTime (em segundos).
     */
    public void update(double deltaTime) {
        playTime += deltaTime;
    }

    /**
     * Retorna o tempo total em segundos.
     */
    public double getPlayTime() {
        return playTime;
    }

    /**
     * Reseta o tempo de jogo.
     */
    public void reset() {
        playTime = 0;
    }
}
