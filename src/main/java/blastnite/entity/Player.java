package blastnite.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import blastnite.level.Platform;

/**
 * The player: runs, jumps, shoots and has a few hearts.
 * It knows nothing about the keyboard: the world tells it what to do.
 */
public class Player extends Creature {
    public static final double WIDTH = 28;
    public static final double HEIGHT = 36;
    public static final int MAX_HEARTS = 3;

    private static final double RUN_SPEED = 260;        // pixels per second
    private static final double JUMP_SPEED = 760;       // upward speed at the start of a jump
    private static final double SHOOT_COOLDOWN = 0.3;   // seconds between shots
    private static final double INVINCIBLE_TIME = 1.0;  // seconds of safety after getting hit
    private static final double HIT_BOUNCE = 350;       // little hop when you get hit

    private int runDirection;   // -1 = left, 0 = standing still, +1 = right
    private int facing = 1;     // which way we look and shoot: -1 = left, +1 = right
    private int hearts = MAX_HEARTS;
    private double shootTimer;
    private double invincibleTimer;

    public Player(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
    }

    /** Creates a player standing on the floor at a level's foot point. */
    public static Player standingAt(double centerX, double bottomY) {
        return new Player(centerX - WIDTH / 2, bottomY - HEIGHT);
    }

    public void setRunDirection(int direction) {
        runDirection = direction;
        if (direction != 0) {
            facing = direction;
        }
    }

    /** Starts a jump, but only if we are standing on something. */
    public void jump() {
        if (onGround) {
            velocityY = -JUMP_SPEED;
            onGround = false;
        }
    }

    @Override
    public void update(double dt, List<Platform> platforms) {
        shootTimer = Math.max(0, shootTimer - dt);
        invincibleTimer = Math.max(0, invincibleTimer - dt);

        moveX(runDirection * RUN_SPEED * dt, platforms);
        fall(dt, platforms);
    }

    public boolean canShoot() {
        return shootTimer == 0 && !isDead();
    }

    /** Fires a bullet from the front of the player, in the direction we are facing. */
    public Bullet shoot() {
        shootTimer = SHOOT_COOLDOWN;
        double bulletX = facing > 0 ? x + width : x - Bullet.WIDTH;
        double bulletY = y + height / 2 - Bullet.HEIGHT / 2;
        return Bullet.fromPlayer(bulletX, bulletY, facing);
    }

    /** Lose a heart, unless we were hit very recently. Losing the last heart kills the player. */
    public void takeHit() {
        if (invincibleTimer > 0 || isDead()) {
            return;
        }
        hearts--;
        invincibleTimer = INVINCIBLE_TIME;
        velocityY = -HIT_BOUNCE;
        onGround = false;
        if (hearts <= 0) {
            kill();
        }
    }

    public int getHearts() {
        return hearts;
    }

    @Override
    public void draw(Graphics2D g) {
        // Blink while invincible: skip drawing every other tenth of a second.
        if (invincibleTimer > 0 && (int) (invincibleTimer * 10) % 2 == 0) {
            return;
        }
        g.setColor(new Color(90, 170, 255));
        g.fillRect((int) x, (int) y, (int) width, (int) height);
        g.setColor(Color.WHITE);
        g.drawRect((int) x, (int) y, (int) width, (int) height);

        // An eye on the side we are facing, so you can see where you will shoot.
        int eyeX = facing > 0 ? (int) (x + width - 10) : (int) (x + 4);
        g.setColor(new Color(20, 24, 40));
        g.fillRect(eyeX, (int) y + 9, 6, 6);
    }
}
