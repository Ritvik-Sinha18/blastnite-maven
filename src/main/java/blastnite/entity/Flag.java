package blastnite.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import blastnite.level.Platform;

/** The goal at the end of the level. Touch it to win. */
public class Flag extends Entity {
    private static final double WIDTH = 30;
    private static final double HEIGHT = 120;

    public Flag(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
    }

    /** Creates a flag standing on the floor at a level's foot point. */
    public static Flag standingAt(double centerX, double bottomY) {
        return new Flag(centerX - WIDTH / 2, bottomY - HEIGHT);
    }

    @Override
    public void update(double dt, List<Platform> platforms) {
        // A flag does not move.
    }

    @Override
    public void draw(Graphics2D g) {
        int poleX = (int) x + 4;
        g.setColor(new Color(200, 210, 230));
        g.fillRect(poleX, (int) y, 4, (int) height);

        int[] xs = {poleX + 4, poleX + 30, poleX + 4};
        int[] ys = {(int) y + 4, (int) y + 18, (int) y + 32};
        g.setColor(new Color(255, 154, 60));
        g.fillPolygon(xs, ys, 3);
    }
}
