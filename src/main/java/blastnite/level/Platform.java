package blastnite.level;

/**
 * A solid rectangle that creatures stand on and bump into.
 * The Level builds these from the '#' blocks in the level file.
 * It is pure data: the Level draws the blocks, this class is only for collisions.
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

    /** True if the point (px, py) lies inside this platform. */
    public boolean contains(double px, double py) {
        return px >= x && px < x + width && py >= y && py < y + height;
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
