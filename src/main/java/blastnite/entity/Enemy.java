package blastnite.entity;

/**
 * Something that hurts the player when touched and can be shot.
 * Each kind of enemy (Walker, Shooter) decides how it moves and how many
 * hits it can take; the world treats them all the same through this class.
 */
public abstract class Enemy extends Creature {
    private static final double FLASH_TIME = 0.12;   // seconds to flash white after a hit

    private int hitPoints;
    private double flashTimer;

    protected Enemy(double x, double y, double width, double height, int hitPoints) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
    }

    /** Called when a player bullet hits this enemy. */
    public void takeHit() {
        hitPoints--;
        flashTimer = FLASH_TIME;
        if (hitPoints <= 0) {
            kill();
        }
    }

    /** Points the player gets for defeating this enemy. */
    public abstract int getPoints();

    /**
     * Gives the enemy a chance to shoot at the player this tick.
     * Most enemies never shoot, so by default this returns null (no bullet).
     */
    public Bullet tryShoot(Player target) {
        return null;
    }

    /** Counts down the white flash; call at the start of update. */
    protected void updateFlash(double dt) {
        flashTimer = Math.max(0, flashTimer - dt);
    }

    protected boolean isFlashing() {
        return flashTimer > 0;
    }
}
