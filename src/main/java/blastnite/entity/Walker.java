package blastnite.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import blastnite.level.Platform;

/**
 * An enemy that walks back and forth. It turns around when it bumps into
 * a wall or reaches the edge of the platform it is standing on.
 * Touching it hurts the player; one bullet kills it.
 */
public class Walker extends Enemy {
    public static final double SIZE = 30;
    private static final double SPEED = 80;   // pixels per second
    private static final int HIT_POINTS = 1;
    private static final int POINTS = 50;

    private int direction = -1;   // starts walking left, towards the player

    public Walker(double x, double y) {
        super(x, y, SIZE, SIZE, HIT_POINTS);
    }

    /** Creates a walker standing on the floor at a level's foot point. */
    public static Walker standingAt(double centerX, double bottomY) {
        return new Walker(centerX - SIZE / 2, bottomY - SIZE);
    }

    @Override
    public void update(double dt, List<Platform> platforms) {
        updateFlash(dt);
        if (moveX(direction * SPEED * dt, platforms)) {
            direction = -direction;   // bumped into a wall
        }
        fall(dt, platforms);
        if (onGround && !groundAhead(platforms)) {
            direction = -direction;   // about to walk off an edge
        }
    }

    /** Looks just in front of our feet: is there floor there? */
    private boolean groundAhead(List<Platform> platforms) {
        double probeX = direction > 0 ? x + width + 1 : x - 1;
        double probeY = y + height + 1;
        return isSolidAt(probeX, probeY, platforms);
    }

    @Override
    public int getPoints() {
        return POINTS;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(isFlashing() ? Color.WHITE : new Color(255, 154, 60));
        g.fillRoundRect((int) x, (int) y, (int) width, (int) height, 10, 10);

        // Two eyes looking in the walking direction.
        g.setColor(new Color(22, 26, 43));
        int lookX = direction > 0 ? 4 : 0;
        g.fillRect((int) x + 6 + lookX, (int) y + 8, 5, 5);
        g.fillRect((int) x + 16 + lookX, (int) y + 8, 5, 5);
    }
}
