package blastnite.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import blastnite.core.GameState;
import blastnite.core.GameWorld;
import blastnite.entity.Player;

/**
 * Draws everything that stays fixed on the screen on top of the game:
 * hearts, the controls hint, and the win / game over screens.
 */
public class Hud {
    private static final Color HEART_FULL = new Color(255, 92, 110);
    private static final Color HEART_EMPTY = new Color(70, 76, 98);
    private static final Color TEXT = new Color(220, 226, 240);

    private final Font smallFont = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    private final Font titleFont = new Font(Font.SANS_SERIF, Font.BOLD, 64);
    private final Font subtitleFont = new Font(Font.SANS_SERIF, Font.PLAIN, 22);

    public void draw(Graphics2D g, GameWorld world) {
        drawHearts(g, world.getPlayer());
        drawControlsHint(g);

        if (world.getState() == GameState.WON) {
            drawOverlay(g, "YOU WIN!", "Press R to play again", new Color(120, 220, 140));
        } else if (world.getState() == GameState.GAME_OVER) {
            drawOverlay(g, "GAME OVER", "Press R to try again", HEART_FULL);
        }
    }

    private void drawHearts(Graphics2D g, Player player) {
        for (int i = 0; i < Player.MAX_HEARTS; i++) {
            g.setColor(i < player.getHearts() ? HEART_FULL : HEART_EMPTY);
            drawHeart(g, 16 + i * 34, 14);
        }
    }

    /** A simple heart shape: two circles on top of a triangle. */
    private void drawHeart(Graphics2D g, int x, int y) {
        g.fillOval(x, y, 15, 15);
        g.fillOval(x + 12, y, 15, 15);
        int[] xs = {x + 1, x + 26, x + 13};
        int[] ys = {y + 10, y + 10, y + 26};
        g.fillPolygon(xs, ys, 3);
    }

    private void drawControlsHint(Graphics2D g) {
        g.setFont(smallFont);
        g.setColor(TEXT);
        g.drawString("A / D  run     Space  jump     Click  shoot", 16, GameWorld.HEIGHT - 14);
    }

    private void drawOverlay(Graphics2D g, String title, String subtitle, Color titleColor) {
        g.setColor(new Color(10, 12, 20, 180));   // see-through dark layer over the game
        g.fillRect(0, 0, GameWorld.WIDTH, GameWorld.HEIGHT);

        g.setFont(titleFont);
        g.setColor(titleColor);
        drawCentered(g, title, GameWorld.HEIGHT / 2 - 10);

        g.setFont(subtitleFont);
        g.setColor(TEXT);
        drawCentered(g, subtitle, GameWorld.HEIGHT / 2 + 40);
    }

    private void drawCentered(Graphics2D g, String text, int y) {
        FontMetrics metrics = g.getFontMetrics();
        int x = (GameWorld.WIDTH - metrics.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }
}
