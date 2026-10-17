package blastnite.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import blastnite.level.Platform;

/**
 * An enemy that stands still, turns to face the player, and fires a slow
 * bullet every couple of seconds when the player is close. Takes two hits.
 */
public class Shooter extends Enemy {
    public static final double WIDTH = 32;
    public static final double HEIGHT = 40;
    private static final int HIT_POINTS = 2;
    private static final int POINTS = 100;
    private static final double FIRE_INTERVAL = 1.8;   // seconds between shots
    private static final double RANGE_X = 520;         // only shoots when the player is this close sideways
    private static final double RANGE_Y = 90;          // ...and roughly at the same height

    private int facing = -1;
    private double cooldown = 1.0;   // short wait before the first shot

    public Shooter(double x, double y) {
        super(x, y, WIDTH, HEIGHT, HIT_POINTS);
    }

    /** Creates a shooter standing on the floor at a level's foot point. */
    public static Shooter standingAt(double centerX, double bottomY) {
        return new Shooter(centerX - WIDTH / 2, bottomY - HEIGHT);
    }

    @Override
    public void update(double dt, List<Platform> platforms) {
        updateFlash(dt);
        cooldown = Math.max(0, cooldown - dt);
        fall(dt, platforms);   // it doesn't walk, but it still stands on the ground
    }

    @Override
    public Bullet tryShoot(Player target) {
        double dx = (target.getX() + target.getWidth() / 2) - (x + width / 2);
        double dy = (target.getY() + target.getHeight() / 2) - (y + height / 2);
        facing = dx < 0 ? -1 : 1;

        if (cooldown > 0 || Math.abs(dx) > RANGE_X || Math.abs(dy) > RANGE_Y) {
            return null;
        }
        cooldown = FIRE_INTERVAL;
        double bulletX = facing > 0 ? x + width : x - Bullet.WIDTH;
        double bulletY = y + 14;
        return Bullet.fromEnemy(bulletX, bulletY, facing);
    }

    @Override
    public int getPoints() {
        return POINTS;
    }

    @Override
    public void draw(Graphics2D g) {
        // Body
        g.setColor(isFlashing() ? Color.WHITE : new Color(176, 112, 255));
        g.fillRoundRect((int) x, (int) y, (int) width, (int) height, 8, 8);

        // Barrel on the side it is facing
        g.setColor(new Color(90, 60, 140));
        int barrelX = facing > 0 ? (int) (x + width - 2) : (int) (x - 10);
        g.fillRect(barrelX, (int) y + 13, 12, 8);

        // One angry eye
        g.setColor(new Color(22, 26, 43));
        int eyeX = facing > 0 ? (int) (x + width - 13) : (int) (x + 6);
        g.fillRect(eyeX, (int) y + 6, 7, 5);
    }
}
