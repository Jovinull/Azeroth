package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import objects.OBJ_Key;

/**
 * Classe responsável por desenhar elementos visuais da interface do usuário
 * (HUD, mensagens, fim de jogo, etc.).
 * Utiliza o contexto de renderização fornecido pelo GamePanel.
 */
public class UI {

    GamePanel gp;

    // Fontes utilizadas para renderização de texto
    Font arial_40, arial_80B;

    // Imagem do item chave para exibição no HUD
    BufferedImage keyImage;

    // Controle de mensagens temporárias exibidas na tela
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;

    // Indica se o jogo foi finalizado
    public boolean gameFinished = false;

    // Tempo de jogo acumulado
    double playTime;

    // Formatação do tempo em segundos com duas casas decimais
    DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    /**
     * Construtor da UI que recebe o GamePanel principal.
     * Inicializa fontes e recursos gráficos utilizados.
     */
    public UI(GamePanel gp) {
        this.gp = gp;

        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);

        // Carrega a imagem do objeto chave para o HUD
        OBJ_Key key = new OBJ_Key();
        keyImage = key.image;
    }

    /**
     * Exibe uma mensagem temporária na tela.
     *
     * @param text Texto da mensagem
     */
    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    /**
     * Renderiza todos os elementos visuais da interface do usuário.
     *
     * @param g2 Objeto gráfico 2D utilizado para renderização
     */
    public void draw(Graphics2D g2) {
        if (gameFinished) {
            // Mensagem de finalização de jogo
            g2.setFont(arial_40);
            g2.setColor(Color.WHITE);

            String text = "Você encontrou o baú!";
            int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            int x = Config.SCREEN_WIDTH / 2 - textLength / 2;
            int y = Config.SCREEN_HEIGHT / 2 - (Config.TILE_SIZE * 3);
            g2.drawString(text, x, y);

            // Exibe tempo de jogo
            text = "Seu Tempo foi: " + decimalFormat.format(gp.gameState.getPlayTime()) + "!";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = Config.SCREEN_WIDTH / 2 - textLength / 2;
            y = Config.SCREEN_HEIGHT / 2 + (Config.TILE_SIZE * 4);
            g2.drawString(text, x, y);

            // Mensagem de parabéns
            g2.setFont(arial_80B);
            g2.setColor(Color.YELLOW);
            text = "Parabéns!";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = Config.SCREEN_WIDTH / 2 - textLength / 2;
            y = Config.SCREEN_HEIGHT / 2 + (Config.TILE_SIZE * 2);
            g2.drawString(text, x, y);

            // Para o loop principal do jogo
            gp.stopGameThread();
        } else {
            // HUD: chave e tempo
            g2.setFont(arial_40);
            g2.setColor(Color.WHITE);
            g2.drawImage(keyImage, Config.TILE_SIZE / 2, Config.TILE_SIZE / 2,
                    Config.TILE_SIZE, Config.TILE_SIZE, null);
            g2.drawString("x " + gp.player.hasKey, Config.UI_KEY_TEXT_X, Config.UI_KEY_TEXT_Y);

            // Atualiza o tempo de jogo (assumindo 60 FPS fixos)
            gp.gameState.update(1.0 / Config.FPS);

            // Mensagens temporárias
            if (messageOn) {
                g2.setFont(g2.getFont().deriveFont((float) Config.UI_MESSAGE_FONT_SIZE));
                g2.drawString(message, Config.TILE_SIZE / 2, Config.TILE_SIZE * 5);

                messageCounter++;
                if (messageCounter > Config.UI_MESSAGE_DURATION_FRAMES) {
                    messageCounter = 0;
                    messageOn = false;
                }
            }
        }
    }
}
