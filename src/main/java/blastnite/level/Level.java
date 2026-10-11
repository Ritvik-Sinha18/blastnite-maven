package blastnite.level;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A level loaded from a text file, where every character is one tile:
 *
 *   #  solid block        P  player start
 *   .  empty air          E  walker enemy
 *   F  goal flag
 *
 * The level itself never changes while playing, so it is loaded once.
 * Spawn points are stored as "foot points": the x of the middle of the tile
 * and the y of the bottom of the tile, so things stand on the floor.
 */
public class Level {
    public static final int TILE_SIZE = 40;

    private static final Color BLOCK = new Color(58, 66, 98);
    private static final Color BLOCK_EDGE = new Color(46, 52, 78);
    private static final Color BLOCK_TOP = new Color(96, 110, 156);

    private final List<String> rows;
    private final int columns;
    private final List<Platform> platforms = new ArrayList<>();
    private final List<Point2D.Double> enemySpawns = new ArrayList<>();
    private Point2D.Double playerStart;
    private Point2D.Double flagPosition;

    private Level(List<String> lines) {
        int widest = 0;
        for (String line : lines) {
            widest = Math.max(widest, line.length());
        }
        columns = widest;

        // Pad short rows with air so every row has the same length.
        rows = new ArrayList<>();
        for (String line : lines) {
            rows.add(line + ".".repeat(columns - line.length()));
        }
        parse();

        if (playerStart == null) {
            throw new IllegalStateException("The level file has no 'P' (player start).");
        }
        if (flagPosition == null) {
            throw new IllegalStateException("The level file has no 'F' (goal flag).");
        }
    }

    /** Loads a level from a file in src/main/resources, e.g. "/levels/level1.txt". */
    public static Level load(String resourcePath) {
        InputStream stream = Level.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new IllegalStateException("Level file not found: " + resourcePath);
        }
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read level file: " + resourcePath, e);
        }
        return new Level(lines);
    }

    private void parse() {
        for (int row = 0; row < rows.size(); row++) {
            int col = 0;
            while (col < columns) {
                char tile = tileAt(row, col);
                if (tile == '#') {
                    // Merge a run of blocks next to each other into one wide platform.
                    int start = col;
                    while (col < columns && tileAt(row, col) == '#') {
                        col++;
                    }
                    platforms.add(new Platform(start * TILE_SIZE, row * TILE_SIZE,
                            (col - start) * TILE_SIZE, TILE_SIZE));
                    continue;
                }
                Point2D.Double foot = new Point2D.Double(
                        col * TILE_SIZE + TILE_SIZE / 2.0, (row + 1) * TILE_SIZE);
                if (tile == 'P') {
                    playerStart = foot;
                } else if (tile == 'E') {
                    enemySpawns.add(foot);
                } else if (tile == 'F') {
                    flagPosition = foot;
                }
                col++;
            }
        }
    }

    private char tileAt(int row, int col) {
        if (row < 0 || row >= rows.size() || col < 0 || col >= columns) {
            return '.';
        }
        return rows.get(row).charAt(col);
    }

    /** Draws only the blocks between fromX and toX (the part the camera can see). */
    public void draw(Graphics2D g, double fromX, double toX) {
        int firstCol = Math.max(0, (int) (fromX / TILE_SIZE));
        int lastCol = Math.min(columns - 1, (int) (toX / TILE_SIZE));
        for (int row = 0; row < rows.size(); row++) {
            for (int col = firstCol; col <= lastCol; col++) {
                if (tileAt(row, col) != '#') {
                    continue;
                }
                int px = col * TILE_SIZE;
                int py = row * TILE_SIZE;
                g.setColor(BLOCK);
                g.fillRect(px, py, TILE_SIZE, TILE_SIZE);
                g.setColor(BLOCK_EDGE);
                g.drawRect(px, py, TILE_SIZE - 1, TILE_SIZE - 1);
                if (tileAt(row - 1, col) != '#') {
                    g.setColor(BLOCK_TOP);          // a lighter top edge where you can stand
                    g.fillRect(px, py, TILE_SIZE, 6);
                }
            }
        }
    }

    public List<Platform> getPlatforms() {
        return Collections.unmodifiableList(platforms);
    }

    public List<Point2D.Double> getEnemySpawns() {
        return Collections.unmodifiableList(enemySpawns);
    }

    public Point2D.Double getPlayerStart() {
        return playerStart;
    }

    public Point2D.Double getFlagPosition() {
        return flagPosition;
    }

    /** Width of the whole level in pixels. */
    public double getWidth() {
        return columns * TILE_SIZE;
    }

    /** Height of the whole level in pixels. Falling below this means falling into a pit. */
    public double getHeight() {
        return rows.size() * TILE_SIZE;
    }
}
