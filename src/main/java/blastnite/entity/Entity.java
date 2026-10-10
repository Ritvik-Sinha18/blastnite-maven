package blastnite.entity;

import java.awt.Graphics2D;

/**
 * Base class for everything that lives in the arena
 * (player now; enemies, projectiles and XP gems later).
 * Every entity is treated as a circle, which keeps collisions simple.
 */
public abstract class Entity {
    protected double x;
    protected double y;
    protected final double radius;

    protected Entity(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    /** Advance this entity by dt seconds. */
    public abstract void update(double dt);

    public abstract void draw(Graphics2D g);

    /** Keeps the whole circle inside a width x height area. */
    public void clampInside(double width, double height) {
        x = Math.max(radius, Math.min(width - radius, x));
        y = Math.max(radius, Math.min(height - radius, y));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }
}
