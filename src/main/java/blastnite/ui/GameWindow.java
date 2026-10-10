package blastnite.ui;

import javax.swing.JFrame;


public class GameWindow extends JFrame {
    private final GamePanel gamePanel;

    public GameWindow() {
        super("Blastnite");
        gamePanel = new GamePanel();
        add(gamePanel);
        pack();                         
        setResizable(false);
        setLocationRelativeTo(null);     
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void startGame() {
        gamePanel.start();
    }
}
