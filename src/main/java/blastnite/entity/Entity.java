package blastnite.entity;

import java.awt.Graphics2D;
import java.util.List;
import blastnite.level.Platform;

/**
 * Base class for everything that lives in the level:
 * the player, enemies, bullets and the goal flag.
 * Every entity is a rectangle: (x, y) is its top-left corner.
 */
public abstract class Entity {
    protected double x;
    protected double y;
    protected final double width;
    protected final double height;
    private boolean dead;

    protected Entity(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /** Advance this entity by dt seconds. Platforms are passed in so it can collide with them. */
    public abstract void update(double dt, List<Platform> platforms);

    public abstract void draw(Graphics2D g);

    /** True if this entity's rectangle overlaps another entity's rectangle. */
    public boolean intersects(Entity other) {
        return overlapsRect(other.x, other.y, other.width, other.height);
    }

    /** True if this entity's rectangle overlaps the platform. */
    protected boolean overlaps(Platform p) {
        return overlapsRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
    }

    private boolean overlapsRect(double ox, double oy, double ow, double oh) {
        return x < ox + ow && x + width > ox && y < oy + oh && y + height > oy;
    }

    /** Keeps the entity between minX and maxX (the left and right edge of the level). */
    public void keepInsideX(double minX, double maxX) {
        x = Math.max(minX, Math.min(maxX - width, x));
    }

    /** Marks this entity as dead; the world removes it after the update loop. */
    public void kill() {
        dead = true;
    }

    public boolean isDead() {
        return dead;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
