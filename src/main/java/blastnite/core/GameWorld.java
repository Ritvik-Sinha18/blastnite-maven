package blastnite.core;

import java.awt.Color;
import java.awt.Graphics2D;
import blastnite.entity.Player;
import blastnite.input.InputHandler;


public class GameWorld {
    public static final int WIDTH = 960;
    public static final int HEIGHT = 640;
    private static final int GRID_SPACING = 40;

    private final Player player;

    public GameWorld() {
        player = new Player(WIDTH / 2.0, HEIGHT / 2.0);
    }

    public void update(double dt, InputHandler input) {
        player.setRunDirection(input.getMoveX());
        if (input.isJumpHeld()) {
            player.jump();
        }
        player.update(dt);
        player.landOnFloor(HEIGHT); 
        player.clampInside(WIDTH, HEIGHT);
    }

    public void draw(Graphics2D g) {
        drawArena(g);
        player.draw(g);
    }

    private void drawArena(Graphics2D g) {
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