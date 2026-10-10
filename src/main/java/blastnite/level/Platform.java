package blastnite.level;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A solid rectangle the player can stand on and bump into
 * (ground, floating platforms, walls).
 */
public class Platform {
    private final double x;
    private final double y;
    private final double width;
    private final double height;

    public Platform(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(58, 66, 98));
        g.fillRect((int) x, (int) y, (int) width, (int) height);
        g.setColor(new Color(90, 102, 145));
        g.fillRect((int) x, (int) y, (int) width, 6);   // lighter strip on top
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