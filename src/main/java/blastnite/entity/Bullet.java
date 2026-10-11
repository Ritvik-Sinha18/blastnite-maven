package blastnite.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import blastnite.level.Platform;

/**
 * A bullet flying straight left or right. It disappears when it
 * hits a wall, hits an enemy, or has flown far enough.
 */
public class Bullet extends Entity {
    public static final double WIDTH = 12;
    public static final double HEIGHT = 4;
    private static final double SPEED = 650;          // pixels per second
    private static final double MAX_DISTANCE = 650;   // about the width of the screen

    private final int direction;   // -1 = left, +1 = right
    private double distanceFlown;

    public Bullet(double x, double y, int direction) {
        super(x, y, WIDTH, HEIGHT);
        this.direction = direction;
    }

    @Override
    public void update(double dt, List<Platform> platforms) {
        double dx = direction * SPEED * dt;
        x += dx;
        distanceFlown += Math.abs(dx);
        if (distanceFlown > MAX_DISTANCE) {
            kill();
            return;
        }
        for (Platform p : platforms) {
            if (overlaps(p)) {
                kill();
                return;
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(255, 214, 90));
        g.fillRect((int) x, (int) y, (int) width, (int) height);
    }
}
