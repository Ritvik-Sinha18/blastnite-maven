package blastnite.entity;

import java.awt.Graphics2D;
import java.util.List;
import blastnite.level.Platform;

/**
 * Base class for everything that moves around the level
 * (the player now; enemies and bullets later).
 * Every entity is a rectangle: (x, y) is its top-left corner.
 */
public abstract class Entity {
    protected double x;
    protected double y;
    protected final double width;
    protected final double height;

    protected Entity(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /** Advance this entity by dt seconds. Platforms are passed in so it can collide with them. */
    public abstract void update(double dt, List<Platform> platforms);

    public abstract void draw(Graphics2D g);

    /** True if this entity's rectangle overlaps the platform's rectangle. */
    protected boolean overlaps(Platform p) {
        return x < p.getX() + p.getWidth()
            && x + width > p.getX()
            && y < p.getY() + p.getHeight()
            && y + height > p.getY();
    }

    /**
     * Moves sideways by dx, then pushes the entity back out of any platform it ended up inside.
     * Returns true if it bumped into something.
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
     * Moves up or down by dy, then pushes the entity back out of any platform it ended up inside.
     * Returns true if it bumped into something (landed on a floor or hit a ceiling).
     */
    protected boolean moveY(double dy, List<Platform> platforms) {
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
        return hit;
    }

    /** Keeps the whole rectangle inside a width x height area (the window, for now). */
    public void clampInside(double areaWidth, double areaHeight) {
        x = Math.max(0, Math.min(areaWidth - width, x));
        y = Math.max(0, Math.min(areaHeight - height, y));
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