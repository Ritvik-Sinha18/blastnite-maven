package blastnite.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import blastnite.level.Platform;

/**
 * The player. Runs left/right, jumps, is pulled down by gravity,
 * and collides with platforms. It knows nothing about the keyboard:
 * the world tells it what to do.
 */
public class Player extends Entity {
    private static final double WIDTH = 28;
    private static final double HEIGHT = 36;
    private static final double RUN_SPEED = 260;       // pixels per second
    private static final double GRAVITY = 1800;        // pixels per second, added every second
    private static final double JUMP_SPEED = 760;      // upward speed at the start of a jump
    private static final double MAX_FALL_SPEED = 900;  // so we never fall fast enough to skip through a platform

    private int runDirection;     // -1 = left, 0 = standing still, +1 = right
    private double velocityY;     // vertical speed; negative = moving up
    private boolean onGround;

    public Player(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
    }

    public void setRunDirection(int direction) {
        runDirection = direction;
    }

    /** Starts a jump, but only if we are standing on something. */
    public void jump() {
        if (onGround) {
            velocityY = -JUMP_SPEED;   // negative because y grows downward on screen
            onGround = false;
        }
    }

    @Override
    public void update(double dt, List<Platform> platforms) {
        // 1. Move sideways first, and stop at walls.
        moveX(runDirection * RUN_SPEED * dt, platforms);

        // 2. Then move up/down with gravity, and stop at floors and ceilings.
        velocityY = Math.min(velocityY + GRAVITY * dt, MAX_FALL_SPEED);
        double dy = velocityY * dt;
        boolean hit = moveY(dy, platforms);

        onGround = hit && dy > 0;   // we only stand on something if we hit it while falling
        if (hit) {
            velocityY = 0;          // landed, or bumped our head: stop moving vertically
        }
    }

    public boolean isOnGround() {
        return onGround;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(90, 170, 255));
        g.fillRect((int) x, (int) y, (int) width, (int) height);
        g.setColor(Color.WHITE);
        g.drawRect((int) x, (int) y, (int) width, (int) height);
    }
}