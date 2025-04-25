package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Classe principal responsável por iniciar a aplicação Swing.
 * Atua como ponto de entrada da aplicação Java.
 */
public class Main {

    /**
     * Método main - ponto de entrada da aplicação.
     * Garante que a interface gráfica seja criada na Event Dispatch Thread (EDT),
     * prevenindo problemas de concorrência e renderização incorreta.
     *
     * @param args argumentos da linha de comando (não utilizados neste contexto).
     */
    public static void main(String[] args) {
        // Executa a criação da interface gráfica na Event Dispatch Thread (EDT)
        // para evitar problemas de concorrência no Swing.
        SwingUtilities.invokeLater(Main::startApplication);
    }

    /**
     * Método responsável por inicializar e exibir a janela principal da aplicação.
     * Essa separação melhora a organização do código e facilita a testabilidade
     * e futuras refatorações.
     */
    public static void startApplication() {
        // Cria a janela principal da aplicação
        JFrame window = new JFrame();

        // Define a operação padrão ao fechar a janela (encerra a aplicação
        // completamente)
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Impede que o usuário redimensione a janela, garantindo layout consistente e
        // estático
        window.setResizable(false);

        // Define o título da janela exibido na barra superior
        window.setTitle("Azeroth");

        // Cria o painel principal do jogo, responsável por renderização e lógica
        GamePanel gamePanel = new GamePanel();

        // [COMENTÁRIO ADICIONAL]
        // Associa o painel de jogo ao JFrame.
        // Isso define a área da interface onde os gráficos e interações ocorrerão.
        window.add(gamePanel);

        // Ajusta o tamanho da janela automaticamente com base no conteúdo do painel
        window.pack();

        // [COMENTÁRIO ADICIONAL]
        // Essa chamada garante que a janela seja exibida centralizada na tela do
        // monitor,
        // melhorando a experiência de abertura inicial.
        window.setLocationRelativeTo(null);

        // Torna a janela visível (última etapa antes da execução do jogo)
        window.setVisible(true);

        // Define o titulo do jogo
        window.setTitle(Config.WINDOW_TITLE);

        // [COMENTÁRIO ADICIONAL]
        // Inicia a lógica de atualização e renderização contínua.
        // Esse método inicia o loop principal de jogo em uma thread separada.
        gamePanel.startGameThread();
    }
}
