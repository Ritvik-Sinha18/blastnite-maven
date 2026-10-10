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
import blastnite.core.GameWorld;
import blastnite.input.InputHandler;


public class GamePanel extends JPanel {
    private static final int TICK_MS = 16;      
    private static final double MAX_DT = 0.05;  

    private final InputHandler input;
    private final GameWorld world;
    private final Timer timer;

    private long lastTickNanos;

    private long tickCount;
    private int framesThisSecond;
    private double secondAccumulator;
    private int fps;

    public GamePanel() {
        setPreferredSize(new Dimension(GameWorld.WIDTH, GameWorld.HEIGHT));
        setBackground(new Color(24, 26, 32));
        setDoubleBuffered(true);

        input = new InputHandler(this);
        world = new GameWorld();
        timer = new Timer(TICK_MS, e -> tick());
    }

    public void start() {
        lastTickNanos = System.nanoTime();
        timer.start();
    }

    private void tick() {
        long now = System.nanoTime();
        double dt = Math.min((now - lastTickNanos) / 1_000_000_000.0, MAX_DT);
        lastTickNanos = now;

        world.update(dt, input);
        updateDebugCounters(dt);
        repaint();
    }

    private void updateDebugCounters(double dt) {
        tickCount++;
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
        drawDebugInfo(g2);

        
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawDebugInfo(Graphics2D g) {
        g.setColor(new Color(150, 255, 150));
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        g.drawString("ticks: " + tickCount + "   fps: " + fps, 10, 20);
    }
}
