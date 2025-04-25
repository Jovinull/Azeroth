package utils;

/**
 * FpsMonitor é uma ferramenta utilitária opcional para monitorar
 * a quantidade de frames renderizados por segundo (FPS) no console.
 */
public class FpsMonitor {

    private boolean enabled;
    private int frameCount;
    private long timer;

    /**
     * Construtor do monitor de FPS.
     *
     * @param enabled define se o monitoramento estará ativo ou não.
     */
    public FpsMonitor(boolean enabled) {
        this.enabled = enabled;
        this.timer = System.currentTimeMillis();
    }

    /**
     * Ativa ou desativa dinamicamente o monitoramento de FPS.
     *
     * @param enabled true para ativar, false para desativar
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Deve ser chamado toda vez que um frame for renderizado.
     * Se o monitoramento estiver ativo, exibe o FPS a cada segundo.
     */
    public void frameRendered() {
        if (!enabled) {
            return;
        }

        frameCount++;

        if (System.currentTimeMillis() - timer >= 1000) {
            System.out.println("FPS: " + frameCount);
            frameCount = 0;
            timer += 1000;
        }
    }
}
