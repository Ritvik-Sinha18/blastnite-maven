package blastnite.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Remembers the best score for each level, even after the game is closed.
 * The scores are saved in a small text file in the user's home folder,
 * which works the same on Windows, macOS and Linux.
 * If the file can't be read or written, the game still works; it just doesn't remember.
 */
public class HighScores {
    private static final String FILE_NAME = ".blastnite-highscores.properties";

    private final Path file;
    private final Properties scores = new Properties();

    public HighScores() {
        this(Paths.get(System.getProperty("user.home"), FILE_NAME));
    }

    public HighScores(Path file) {
        this.file = file;
        load();
    }

    /** The best score for a level, or 0 if it has never been played. */
    public int getBest(String levelName) {
        try {
            return Integer.parseInt(scores.getProperty(levelName, "0"));
        } catch (NumberFormatException e) {
            return 0;   // the file was edited by hand into something that isn't a number
        }
    }

    /**
     * Records a finished game. Returns true if it is a new best score for that level.
     */
    public boolean submit(String levelName, int score) {
        if (score <= getBest(levelName)) {
            return false;
        }
        scores.setProperty(levelName, String.valueOf(score));
        save();
        return true;
    }

    private void load() {
        if (!Files.exists(file)) {
            return;   // first time playing: no scores yet
        }
        try (InputStream in = Files.newInputStream(file)) {
            scores.load(in);
        } catch (IOException e) {
            scores.clear();   // unreadable file: start without saved scores
        }
    }

    private void save() {
        try (OutputStream out = Files.newOutputStream(file)) {
            scores.store(out, "Blastnite best scores");
        } catch (IOException e) {
            // Can't save (for example a read-only folder). The game carries on.
        }
    }
}