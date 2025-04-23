package main;

import javax.swing.*;

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

        // Define a operação padrão ao fechar a janela (encerra a aplicação)
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Impede que o usuário redimensione a janela, garantindo layout consistente
        window.setResizable(false);

        // Define o título da janela, útil para identificação da aplicação
        window.setTitle("Azeroth");

        // Cria e adiciona o painel de jogo à janela
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        // Ajusta automaticamente o tamanho da janela de acordo com o conteúdo (preferência dos componentes)
        window.pack();

        // Centraliza a janela na tela, melhorando a experiência do usuário
        window.setLocationRelativeTo(null);

        // Torna a janela visível ao usuário
        window.setVisible(true);
    }
}
