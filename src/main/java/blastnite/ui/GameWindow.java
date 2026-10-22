package blastnite.ui;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The application window. It holds two screens, the menu and the game,
 * and uses a CardLayout to show one of them at a time.
 */
public class GameWindow extends JFrame {
    private static final String MENU = "menu";
    private static final String GAME = "game";

    private final CardLayout cards = new CardLayout();
    private final JPanel screens = new JPanel(cards);
    private final GamePanel gamePanel;

    public GameWindow() {
        super("Blastnite");
        MenuPanel menuPanel = new MenuPanel(
                () -> startGame(0),      // Easy
                () -> startGame(1),      // Difficult
                this::showHowToPlay,
                this::quit);
        gamePanel = new GamePanel(this::showMenu);

        screens.add(menuPanel, MENU);
        screens.add(gamePanel, GAME);
        add(screens);

        pack();                          // size the window to the panels' preferred size
        setResizable(false);
        setLocationRelativeTo(null);     // center on screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void showMenu() {
        gamePanel.stop();
        cards.show(screens, MENU);
    }

    /** Shows the game screen and starts the chosen level (0 = Easy, 1 = Difficult). */
    public void startGame(int chosenLevel) {
        cards.show(screens, GAME);
        gamePanel.startNewGame(chosenLevel);
    }

    private void showHowToPlay() {
        String text = String.join("\n",
                "Choose Easy or Difficult, then reach the flag at the end of the level.",
                "",
                "A / D        run left and right",
                "Space        jump",
                "Left click   shoot",
                "Esc          pause",
                "",
                "Orange walkers: one hit (50 points).",
                "Purple shooters fire back: two hits (100 points).",
                "Coins: 10 points each.",
                "Touching an enemy or getting shot costs a heart. Lose all 3, or fall into a pit, and it's game over.");
        JOptionPane.showMessageDialog(this, text, "How to Play", JOptionPane.INFORMATION_MESSAGE);
    }

    private void quit() {
        dispose();
        System.exit(0);
    }

    GamePanel getGamePanel() {
        return gamePanel;
    }
}