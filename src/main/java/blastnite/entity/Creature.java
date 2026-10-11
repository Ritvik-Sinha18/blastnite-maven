package blastnite.entity;

import java.util.List;
import blastnite.level.Platform;

/**
 * An entity that walks around and is pulled down by gravity:
 * the player and the enemies. Bullets and the flag are not creatures.
 *
 * Collisions are handled one direction at a time: first move sideways and
 * push back out of walls, then move up/down and push back out of floors or
 * ceilings. Doing X and Y separately tells us which side we hit, so a
 * creature never gets stuck in a corner.
 */
public abstract class Creature extends Entity {
    private static final double GRAVITY = 1800;        // pixels per second, added every second
    private static final double MAX_FALL_SPEED = 900;  // so we never fall through a platform in one tick

    protected double velocityY;   // vertical speed; negative = moving up (screen y grows downward)
    protected boolean onGround;

    protected Creature(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    /**
     * Moves sideways by dx, then pushes the creature back out of any platform it ended up inside.
     * Returns true if it bumped into a wall.
     */
    protected boolean moveX(double dx, List<Platform> platforms) {
        x += dx;
        boolean hit = false;
        for (Platform p : platforms) {
            if (overlaps(p)) {
                if (dx > 0) {
                    x = p.getX() - width;              // hit a wall on the right
                } else if (dx < 0) {
                    x = p.getX() + p.getWidth();       // hit a wall on the left
                }
                hit = true;
            }
        }
        return hit;
    }

    /**
     * Applies gravity, moves up or down, and pushes the creature back out of floors and ceilings.
     * Afterwards onGround says whether it is standing on something.
     */
    protected void fall(double dt, List<Platform> platforms) {
        velocityY = Math.min(velocityY + GRAVITY * dt, MAX_FALL_SPEED);
        double dy = velocityY * dt;
        y += dy;

        boolean hit = false;
        for (Platform p : platforms) {
            if (overlaps(p)) {
                if (dy > 0) {
                    y = p.getY() - height;             // landed on top of a platform
                } else if (dy < 0) {
                    y = p.getY() + p.getHeight();      // bumped head on a ceiling
                }
                hit = true;
            }
        }
        onGround = hit && dy > 0;   // only standing if we hit something while falling
        if (hit) {
            velocityY = 0;
        }
    }

    /** True if any platform contains the point (px, py). Used to look for ground ahead. */
    protected static boolean isSolidAt(double px, double py, List<Platform> platforms) {
        for (Platform p : platforms) {
            if (p.contains(px, py)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOnGround() {
        return onGround;
    }
}
