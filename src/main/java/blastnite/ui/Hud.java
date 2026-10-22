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
 * hearts, score, the controls hint, and the pause / win / game over screens.
 */
public class Hud {
    private static final Color HEART_FULL = new Color(255, 92, 110);
    private static final Color HEART_EMPTY = new Color(70, 76, 98);
    private static final Color TEXT = new Color(220, 226, 240);
    private static final Color GOLD = new Color(244, 197, 66);

    private final Font smallFont = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    private final Font scoreFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    private final Font titleFont = new Font(Font.SANS_SERIF, Font.BOLD, 64);
    private final Font subtitleFont = new Font(Font.SANS_SERIF, Font.PLAIN, 22);

    public void draw(Graphics2D g, GameWorld world) {
        drawHearts(g, world.getPlayer());
        drawScore(g, world.getScore());
        drawLevel(g, world.getLevelName(), world.getBestScore());
        drawControlsHint(g);

        String scoreLine = "Score: " + world.getScore() + "     Best: " + world.getBestScore();
        switch (world.getState()) {
            case PAUSED:
                drawOverlay(g, "PAUSED", null, "Esc  resume     R  restart     M  menu", TEXT, false);
                break;
            case WON:
                drawOverlay(g, "YOU WIN!", scoreLine, "R  play again     M  menu",
                        new Color(120, 220, 140), world.isNewBest());
                break;
            case GAME_OVER:
                drawOverlay(g, "GAME OVER", scoreLine, "R  try again     M  menu",
                        HEART_FULL, world.isNewBest());
                break;
            default:
                break;
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

    private void drawScore(Graphics2D g, int score) {
        g.setColor(GOLD);
        g.fillOval(130, 17, 18, 18);
        g.setFont(scoreFont);
        g.setColor(TEXT);
        g.drawString(String.valueOf(score), 156, 33);
    }

    private void drawLevel(Graphics2D g, String name, int best) {
        g.setFont(scoreFont);
        g.setColor(TEXT);
        g.drawString("Level: " + name, 240, 33);
        g.setFont(smallFont);
        g.setColor(GOLD);
        g.drawString("Best: " + best, 440, 32);
    }

    private void drawControlsHint(Graphics2D g) {
        g.setFont(smallFont);
        g.setColor(TEXT);
        g.drawString("A / D  run     Space  jump     Click  shoot     Esc  pause", 16, GameWorld.HEIGHT - 14);
    }

    /**
     * Darkens the game and shows a big title, an optional score line, the keys to press,
     * and "NEW BEST!" if the player just beat their best score.
     */
    private void drawOverlay(Graphics2D g, String title, String scoreLine, String keys,
                             Color titleColor, boolean newBest) {
        g.setColor(new Color(10, 12, 20, 180));   // see-through dark layer over the game
        g.fillRect(0, 0, GameWorld.WIDTH, GameWorld.HEIGHT);

        int y = GameWorld.HEIGHT / 2 - 20;
        if (newBest) {
            g.setFont(subtitleFont);
            g.setColor(GOLD);
            drawCentered(g, "NEW BEST!", y - 70);
        }

        g.setFont(titleFont);
        g.setColor(titleColor);
        drawCentered(g, title, y);

        g.setFont(subtitleFont);
        g.setColor(TEXT);
        if (scoreLine != null) {
            drawCentered(g, scoreLine, y + 50);
            y += 36;
        }
        drawCentered(g, keys, y + 50);
    }

    private void drawCentered(Graphics2D g, String text, int y) {
        FontMetrics metrics = g.getFontMetrics();
        int x = (GameWorld.WIDTH - metrics.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }
}