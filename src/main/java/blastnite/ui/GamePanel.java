package blastnite.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.Timer;
import blastnite.core.GameState;
import blastnite.core.GameWorld;
import blastnite.input.InputHandler;

/**
 * Runs the game loop and draws the game.
 * Each tick: measure elapsed time -> update the world -> repaint.
 */
public class GamePanel extends JPanel {
    private static final int TICK_MS = 16;      // ~60 ticks per second
    // Cap dt so a lag spike can't move anything far enough to skip through a 40 px block.
    private static final double MAX_DT = 0.04;

    private final InputHandler input;
    private final GameWorld world;
    private final Hud hud;
    private final Timer timer;
    private final Runnable onBackToMenu;

    private long lastTickNanos;

    // FPS counter, to show the loop is running smoothly.
    private int framesThisSecond;
    private double secondAccumulator;
    private int fps;

    /** @param onBackToMenu what to do when the player presses M on the pause / end screen */
    public GamePanel(Runnable onBackToMenu) {
        this.onBackToMenu = onBackToMenu;
        setPreferredSize(new Dimension(GameWorld.WIDTH, GameWorld.HEIGHT));
        setBackground(new Color(24, 26, 32));
        setDoubleBuffered(true);
        setFocusable(true);

        input = new InputHandler(this);
        world = new GameWorld();
        hud = new Hud();
        timer = new Timer(TICK_MS, e -> tick());
    }

    /** Starts a fresh game and the game loop. */
    public void startNewGame() {
        world.newGame();
        input.reset();
        lastTickNanos = System.nanoTime();
        timer.start();
        requestFocusInWindow();
    }

    /** Stops the game loop (when we go back to the menu). */
    public void stop() {
        timer.stop();
    }

    private void tick() {
        long now = System.nanoTime();
        double dt = Math.min((now - lastTickNanos) / 1_000_000_000.0, MAX_DT);
        lastTickNanos = now;

        // Going back to the menu is the window's job, not the world's,
        // so the panel handles that key and tells the window.
        if (world.getState() != GameState.PLAYING && input.wasMenuPressed()) {
            input.endTick();
            onBackToMenu.run();
            return;
        }

        world.update(dt, input);
        input.endTick();
        updateFps(dt);
        repaint();
    }

    private void updateFps(double dt) {
        framesThisSecond++;
        secondAccumulator += dt;
        if (secondAccumulator >= 1.0) {
            fps = framesThisSecond;
            framesThisSecond = 0;
            secondAccumulator -= 1.0;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        world.draw(g2);
        hud.draw(g2, world);
        drawFps(g2);

        // Flushes the graphics pipeline; prevents stutter on some Linux setups.
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawFps(Graphics2D g) {
        g.setColor(new Color(150, 255, 150));
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        g.drawString("fps: " + fps, GameWorld.WIDTH - 80, 20);
    }

    GameWorld getWorld() {
        return world;
    }
}
