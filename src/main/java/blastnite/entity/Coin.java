package blastnite.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import blastnite.level.Platform;

/** A coin floating in the air. Touch it to collect it and get points. */
public class Coin extends Entity {
    public static final int VALUE = 10;
    private static final double SIZE = 16;

    private double time;   // for the little up-and-down bobbing animation

    public Coin(double x, double y) {
        super(x, y, SIZE, SIZE);
        time = x / 100;    // different coins bob slightly out of step
    }

    /** Creates a coin centered on a point. */
    public static Coin centeredAt(double centerX, double centerY) {
        return new Coin(centerX - SIZE / 2, centerY - SIZE / 2);
    }

    @Override
    public void update(double dt, List<Platform> platforms) {
        time += dt;
    }

    @Override
    public void draw(Graphics2D g) {
        int bob = (int) Math.round(Math.sin(time * 4) * 3);
        int drawY = (int) y + bob;
        g.setColor(new Color(244, 197, 66));
        g.fillOval((int) x, drawY, (int) width, (int) height);
        g.setColor(new Color(255, 236, 150));
        g.fillOval((int) x + 4, drawY + 3, 5, 5);
    }
}
