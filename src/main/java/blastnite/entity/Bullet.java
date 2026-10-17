package blastnite.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import blastnite.level.Platform;

/**
 * A bullet flying straight left or right. Player bullets hurt enemies,
 * enemy bullets hurt the player. A bullet disappears when it hits a wall,
 * hits its target, or has flown far enough.
 */
public class Bullet extends Entity {
    public static final double WIDTH = 12;
    public static final double HEIGHT = 6;
    private static final double PLAYER_SPEED = 650;   // pixels per second
    private static final double ENEMY_SPEED = 330;    // slower, so the player can dodge
    private static final double MAX_DISTANCE = 650;   // about the width of the screen

    private final int direction;   // -1 = left, +1 = right
    private final double speed;
    private final boolean fromPlayer;
    private double distanceFlown;

    private Bullet(double x, double y, int direction, double speed, boolean fromPlayer) {
        super(x, y, WIDTH, HEIGHT);
        this.direction = direction;
        this.speed = speed;
        this.fromPlayer = fromPlayer;
    }

    /** A bullet fired by the player. */
    public static Bullet fromPlayer(double x, double y, int direction) {
        return new Bullet(x, y, direction, PLAYER_SPEED, true);
    }

    /** A bullet fired by an enemy. */
    public static Bullet fromEnemy(double x, double y, int direction) {
        return new Bullet(x, y, direction, ENEMY_SPEED, false);
    }

    public boolean isFromPlayer() {
        return fromPlayer;
    }

    @Override
    public void update(double dt, List<Platform> platforms) {
        double dx = direction * speed * dt;
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
        if (fromPlayer) {
            g.setColor(new Color(255, 214, 90));
            g.fillRect((int) x, (int) y + 1, (int) width, (int) height - 2);
        } else {
            g.setColor(new Color(255, 90, 120));
            g.fillOval((int) x, (int) y, (int) width, (int) height);
        }
    }
}
