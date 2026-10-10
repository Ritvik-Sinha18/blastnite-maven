package blastnite.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import blastnite.entity.Player;
import blastnite.input.InputHandler;
import blastnite.level.Platform;

/**
 * Holds the game state and the rules for updating it.
 * The GUI (GamePanel) only asks the world to update and to draw itself.
 */
public class GameWorld {
    public static final int WIDTH = 960;
    public static final int HEIGHT = 640;
    private static final int GRID_SPACING = 40;
    private static final int GROUND_HEIGHT = 40;

    private final Player player;
    private final List<Platform> platforms = new ArrayList<>();

    public GameWorld() {
        buildTestLevel();
        player = new Player(80, HEIGHT - GROUND_HEIGHT - 36);
    }

    /**
     * A hand-made test level. Temporary: in backlog item 4 this will be
     * loaded from a text file instead.
     */
    private void buildTestLevel() {
        int groundY = HEIGHT - GROUND_HEIGHT;
        platforms.add(new Platform(0, groundY, WIDTH, GROUND_HEIGHT));   // the ground
        platforms.add(new Platform(600, groundY - 50, 40, 50));          // a small wall to jump over
        platforms.add(new Platform(150, groundY - 100, 200, 30));        // floating platforms,
        platforms.add(new Platform(430, groundY - 190, 180, 30));        // each one about 90 px
        platforms.add(new Platform(680, groundY - 280, 200, 30));        // above the one below
    }

    public void update(double dt, InputHandler input) {
        player.setRunDirection(input.getMoveX());
        if (input.isJumpHeld()) {
            player.jump();
        }
        player.update(dt, platforms);
        player.clampInside(WIDTH, HEIGHT);   // stay inside the window (until we add a camera)
    }

    public void draw(Graphics2D g) {
        drawBackgroundGrid(g);
        for (Platform p : platforms) {
            p.draw(g);
        }
        player.draw(g);
    }

    private void drawBackgroundGrid(Graphics2D g) {
        g.setColor(new Color(36, 39, 48));
        for (int gx = 0; gx <= WIDTH; gx += GRID_SPACING) {
            g.drawLine(gx, 0, gx, HEIGHT);
        }
        for (int gy = 0; gy <= HEIGHT; gy += GRID_SPACING) {
            g.drawLine(0, gy, WIDTH, gy);
        }
    }

    public Player getPlayer() {
        return player;
    }
}