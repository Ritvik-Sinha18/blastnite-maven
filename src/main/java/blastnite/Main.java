package blastnite;

import javax.swing.SwingUtilities;
import blastnite.ui.GameWindow;

/**
 * Entry point. Swing is not thread-safe, so the whole GUI is created
 * on the Event Dispatch Thread (EDT) via invokeLater.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow();
            window.showMenu();
            window.setVisible(true);
        });
    }
}
