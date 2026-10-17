package blastnite.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import blastnite.entity.Bullet;
import blastnite.entity.Coin;
import blastnite.entity.Enemy;
import blastnite.entity.Flag;
import blastnite.entity.Player;
import blastnite.entity.Shooter;
import blastnite.entity.Walker;
import blastnite.input.InputHandler;
import blastnite.level.Level;
import blastnite.level.Platform;

/**
 * Holds the game state and the rules for updating it.
 * The GUI (GamePanel) only asks the world to update and to draw itself.
 */
public class GameWorld {
    /** Size of the visible screen, not of the level (the level can be much wider). */
    public static final int WIDTH = 960;
    public static final int HEIGHT = 640;

    private static final String LEVEL_FILE = "/levels/level1.txt";
    private static final int GRID_SPACING = 40;

    private final Level level;
    private final Camera camera = new Camera(WIDTH);
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<Coin> coins = new ArrayList<>();
    private Player player;
    private Flag flag;
    private GameState state;
    private int score;

    public GameWorld() {
        level = Level.load(LEVEL_FILE);
        newGame();
    }

    /** Puts everything back where the level file says: used for a new game and on restart. */
    public void newGame() {
        Point2D.Double start = level.getPlayerStart();
        player = Player.standingAt(start.x, start.y);

        Point2D.Double goal = level.getFlagPosition();
        flag = Flag.standingAt(goal.x, goal.y);

        enemies.clear();
        for (Point2D.Double spawn : level.getWalkerSpawns()) {
            enemies.add(Walker.standingAt(spawn.x, spawn.y));
        }
        for (Point2D.Double spawn : level.getShooterSpawns()) {
            enemies.add(Shooter.standingAt(spawn.x, spawn.y));
        }

        coins.clear();
        for (Point2D.Double spot : level.getCoinPositions()) {
            coins.add(Coin.centeredAt(spot.x, spot.y));
        }

        bullets.clear();
        score = 0;
        camera.follow(player, level.getWidth());
        state = GameState.PLAYING;
    }

    public void update(double dt, InputHandler input) {
        switch (state) {
            case PAUSED:
                if (input.wasPausePressed()) {
                    state = GameState.PLAYING;
                } else if (input.isRestartHeld()) {
                    newGame();
                }
                return;
            case WON:
            case GAME_OVER:
                if (input.isRestartHeld()) {
                    newGame();
                }
                return;   // nothing moves on the pause / win / game over screen
            case PLAYING:
            default:
                if (input.wasPausePressed()) {
                    state = GameState.PAUSED;
                    return;
                }
                break;
        }

        List<Platform> platforms = level.getPlatforms();

        // 1. The player reacts to the keyboard and mouse.
        player.setRunDirection(input.getMoveX());
        if (input.isJumpHeld()) {
            player.jump();
        }
        if (input.isShootHeld() && player.canShoot()) {
            bullets.add(player.shoot());
        }

        // 2. Everything moves (and enemies that can shoot get a chance to).
        player.update(dt, platforms);
        player.keepInsideX(0, level.getWidth());
        for (Enemy enemy : enemies) {
            enemy.update(dt, platforms);
            Bullet shot = enemy.tryShoot(player);
            if (shot != null) {
                bullets.add(shot);
            }
        }
        for (Bullet bullet : bullets) {
            bullet.update(dt, platforms);
        }
        for (Coin coin : coins) {
            coin.update(dt, platforms);
        }

        // 3. Things hit each other.
        handleCollisions();

        // 4. Remove what died this tick (never while looping over the same list).
        enemies.removeIf(Enemy::isDead);
        bullets.removeIf(Bullet::isDead);
        coins.removeIf(Coin::isDead);

        // 5. Check whether the game is won or lost.
        if (player.isDead()) {
            state = GameState.GAME_OVER;
        } else if (player.intersects(flag)) {
            state = GameState.WON;
        }

        camera.follow(player, level.getWidth());
    }

    private void handleCollisions() {
        for (Bullet bullet : bullets) {
            if (bullet.isFromPlayer()) {
                for (Enemy enemy : enemies) {
                    if (!bullet.isDead() && !enemy.isDead() && bullet.intersects(enemy)) {
                        bullet.kill();
                        enemy.takeHit();
                        if (enemy.isDead()) {
                            score += enemy.getPoints();
                        }
                    }
                }
            } else if (!bullet.isDead() && bullet.intersects(player)) {
                bullet.kill();
                player.takeHit();
            }
        }
        for (Enemy enemy : enemies) {
            if (!enemy.isDead() && enemy.intersects(player)) {
                player.takeHit();
            }
            if (enemy.getY() > level.getHeight()) {
                enemy.kill();   // fell into a pit (no points)
            }
        }
        for (Coin coin : coins) {
            if (!coin.isDead() && coin.intersects(player)) {
                coin.kill();
                score += Coin.VALUE;
            }
        }
        if (player.getY() > level.getHeight()) {
            player.kill();      // fell into a pit
        }
    }

    /** Draws the level and everything in it, shifted by the camera. The HUD is drawn separately. */
    public void draw(Graphics2D g) {
        int cameraX = (int) Math.round(camera.getX());
        g.translate(-cameraX, 0);

        drawBackgroundGrid(g, cameraX);
        level.draw(g, cameraX, cameraX + WIDTH);
        flag.draw(g);
        for (Coin coin : coins) {
            coin.draw(g);
        }
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        player.draw(g);

        g.translate(cameraX, 0);   // undo the shift so the HUD stays fixed on screen
    }

    private void drawBackgroundGrid(Graphics2D g, int cameraX) {
        g.setColor(new Color(36, 39, 48));
        int firstLine = cameraX - cameraX % GRID_SPACING;
        for (int gx = firstLine; gx <= cameraX + WIDTH; gx += GRID_SPACING) {
            g.drawLine(gx, 0, gx, HEIGHT);
        }
        for (int gy = 0; gy <= HEIGHT; gy += GRID_SPACING) {
            g.drawLine(cameraX, gy, cameraX + WIDTH, gy);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public GameState getState() {
        return state;
    }

    public int getScore() {
        return score;
    }

    public int getEnemiesLeft() {
        return enemies.size();
    }

    public int getCoinsLeft() {
        return coins.size();
    }
}
