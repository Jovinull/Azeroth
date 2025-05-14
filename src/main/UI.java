package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import objects.OBJ_Key;

/**
 * Classe responsável por exibir informações visuais ao jogador, como HUD,
 * mensagens e tela de final de jogo.
 */
public class UI {

    GamePanel gp;

    // Fonte padrão para textos na HUD
    Font arial_40, arial_80B;

    // Ícone representando a chave coletada
    BufferedImage keyImage;

    // Controle de exibição de mensagens temporárias na tela
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;

    // Indica se o jogo foi concluído
    public boolean gameFinished = false;

    // Contador de tempo decorrido em segundos, com duas casas decimais
    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");

    public UI(GamePanel gp) {
        this.gp = gp;

        // Inicialização das fontes e do ícone da chave
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);

        // Carrega imagem da chave a partir do objeto correspondente
        OBJ_Key key = new OBJ_Key();
        keyImage = key.image;
    }

    /**
     * Ativa a exibição de uma mensagem temporária no HUD.
     *
     * @param text Texto a ser exibido
     */
    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    /**
     * Renderiza os elementos da interface de usuário com base no estado do jogo.
     *
     * @param g2 Contexto gráfico 2D para renderização
     */
    public void draw(Graphics2D g2) {
        if (gameFinished) {
            // Mensagem de vitória e estatísticas
            g2.setFont(arial_40);
            g2.setColor(Color.white);

            String text;
            int textLength;
            int x;
            int y;

            text = "Você encontrou o tesouro!";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = Config.SCREEN_WIDTH / 2 - textLength / 2;
            y = Config.SCREEN_HEIGHT / 2 - (Config.TILE_SIZE * 3);
            g2.drawString(text, x, y);

            text = "Seu tempo foi:" + dFormat.format(playTime) + "!";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = Config.SCREEN_WIDTH / 2 - textLength / 2;
            y = Config.SCREEN_HEIGHT / 2 + (Config.TILE_SIZE * 4);
            g2.drawString(text, x, y);

            g2.setFont(arial_80B);
            g2.setColor(Color.yellow);
            text = "Parabéns!";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = Config.SCREEN_WIDTH / 2 - textLength / 2;
            y = Config.SCREEN_HEIGHT / 2 + (Config.TILE_SIZE * 2);
            g2.drawString(text, x, y);

            gp.stopGameThread();

        } else {
            // HUD: chaves e tempo
            g2.setFont(arial_40);
            g2.setColor(Color.WHITE);

            g2.drawImage(keyImage, Config.TILE_SIZE / 2, Config.TILE_SIZE / 2, Config.TILE_SIZE, Config.TILE_SIZE,
                    null);
            g2.drawString("x " + gp.player.getHasKey(), 74, 65);

            // Atualização do tempo de jogo
            playTime += (double) 1 / Config.FPS;
            g2.drawString("Tempo:" + dFormat.format(playTime), Config.TILE_SIZE * 11, 65);

            // Exibição de mensagens temporárias
            if (messageOn) {
                g2.setFont(g2.getFont().deriveFont(30F));
                g2.drawString(message, Config.TILE_SIZE / 2, Config.TILE_SIZE * 5);

                messageCounter++;

                if (messageCounter > Config.UI_MESSAGE_DISPLAY_FRAMES) {
                    messageCounter = 0;
                    messageOn = false;
                }
            }
        }
    }
}
