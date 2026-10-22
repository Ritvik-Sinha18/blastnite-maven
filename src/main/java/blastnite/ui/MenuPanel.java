package blastnite.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import blastnite.core.GameWorld;
import blastnite.core.HighScores;

/**
 * The start screen: an animated night scene, the title, and four buttons
 * (Easy, Difficult, How to Play, Quit). The level buttons show the best score.
 * It does not know what the buttons do; the window passes that in.
 */
public class MenuPanel extends JPanel {
    private static final Color SKY_TOP = new Color(14, 16, 30);
    private static final Color SKY_BOTTOM = new Color(38, 44, 78);
    private static final Color HILLS_FAR = new Color(32, 37, 66);
    private static final Color HILLS_NEAR = new Color(26, 30, 54);
    private static final Color TEXT = new Color(235, 238, 248);
    private static final Color TEXT_SOFT = new Color(178, 186, 214);
    private static final Color ACCENT = new Color(255, 154, 60);
    private static final Color EASY = new Color(110, 214, 140);
    private static final Color HARD = new Color(255, 92, 110);
    private static final Color NEUTRAL = new Color(120, 132, 178);
    private static final int GROUND_HEIGHT = 80;

    // These names must match the level names in GameWorld.
    private static final String EASY_NAME = "Easy";
    private static final String HARD_NAME = "Difficult";

    private final HighScores highScores;
    private final MenuButton easyButton;
    private final MenuButton hardButton;
    private final int[][] stars = new int[70][3];   // x, y, size of each star
    private double time;                             // seconds since the menu was created

    public MenuPanel(HighScores highScores, Runnable onEasy, Runnable onHard,
                     Runnable onHowToPlay, Runnable onQuit) {
        this.highScores = highScores;
        setPreferredSize(new Dimension(GameWorld.WIDTH, GameWorld.HEIGHT));
        setBackground(SKY_BOTTOM);
        setLayout(new GridBagLayout());   // centers the column of content

        Random random = new Random(7);   // fixed seed: the stars are in the same place every time
        for (int[] star : stars) {
            star[0] = random.nextInt(GameWorld.WIDTH);
            star[1] = random.nextInt(GameWorld.HEIGHT - 200);
            star[2] = 1 + random.nextInt(2);
        }

        JPanel column = new JPanel();
        column.setOpaque(false);
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));

        JLabel subtitle = new JLabel("Run. Jump. Blast your way to the flag.");
        subtitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        subtitle.setForeground(TEXT_SOFT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        easyButton = new MenuButton("Play: Easy", EASY, onEasy);
        hardButton = new MenuButton("Play: Difficult", HARD, onHard);

        column.add(new TitleLabel());
        column.add(subtitle);
        column.add(Box.createVerticalStrut(34));
        column.add(easyButton);
        column.add(Box.createVerticalStrut(12));
        column.add(hardButton);
        column.add(Box.createVerticalStrut(12));
        column.add(new MenuButton("How to Play", NEUTRAL, onHowToPlay));
        column.add(Box.createVerticalStrut(12));
        column.add(new MenuButton("Quit", NEUTRAL, onQuit));
        column.add(Box.createVerticalStrut(GROUND_HEIGHT + 10));   // keep the buttons above the ground
        add(column);

        refreshScores();

        // A gentle animation: twinkling stars, moving hills, a walking enemy, bobbing coins.
        Timer animation = new Timer(33, e -> {
            time += 0.033;
            if (isShowing()) {
                repaint();
            }
        });
        animation.start();
    }

    /** Updates the "Best" line on the level buttons (called whenever the menu is shown). */
    public void refreshScores() {
        easyButton.setSubtitle(bestText(EASY_NAME));
        hardButton.setSubtitle(bestText(HARD_NAME));
    }

    private String bestText(String levelName) {
        int best = highScores.getBest(levelName);
        return best > 0 ? "Best: " + best : "No score yet";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        int groundY = h - GROUND_HEIGHT;

        // Night sky that gets lighter towards the horizon
        g2.setPaint(new GradientPaint(0, 0, SKY_TOP, 0, groundY, SKY_BOTTOM));
        g2.fillRect(0, 0, w, h);

        // Twinkling stars
        for (int i = 0; i < stars.length; i++) {
            int alpha = (int) (140 + 110 * Math.sin(time * 2 + i));
            g2.setColor(new Color(255, 255, 255, Math.max(30, Math.min(255, alpha))));
            g2.fillRect(stars[i][0], stars[i][1], stars[i][2], stars[i][2]);
        }

        // Two layers of hills that slowly drift sideways
        drawHills(g2, w, groundY, HILLS_FAR, 70, 0.006, time * 8);
        drawHills(g2, w, groundY, HILLS_NEAR, 40, 0.011, time * 18);

        drawGround(g2, w, groundY);
        drawScene(g2, groundY);

        // Controls hint on the ground
        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        g2.setColor(TEXT_SOFT);
        String hint = "A / D  run      Space  jump      Click  shoot      Esc  pause";
        FontMetrics metrics = g2.getFontMetrics();
        g2.drawString(hint, (w - metrics.stringWidth(hint)) / 2, h - 18);
    }

    /** A wavy band of hills, made from a sine curve. */
    private void drawHills(Graphics2D g, int w, int groundY, Color color, int height,
                           double waviness, double offset) {
        int points = w / 8 + 3;
        int[] xs = new int[points];
        int[] ys = new int[points];
        for (int i = 0; i < points - 2; i++) {
            int x = i * 8;
            xs[i] = x;
            ys[i] = groundY - height / 2 - (int) (Math.sin((x + offset) * waviness) * height / 2);
        }
        xs[points - 2] = w;
        ys[points - 2] = groundY;
        xs[points - 1] = 0;
        ys[points - 1] = groundY;
        g.setColor(color);
        g.fillPolygon(xs, ys, points);
    }

    private void drawGround(Graphics2D g, int w, int groundY) {
        for (int x = 0; x < w; x += 40) {
            g.setColor(new Color(58, 66, 98));
            g.fillRect(x, groundY, 40, GROUND_HEIGHT);
            g.setColor(new Color(46, 52, 78));
            g.drawRect(x, groundY, 39, 39);
            g.drawRect(x, groundY + 40, 39, 39);
            g.setColor(new Color(96, 110, 156));
            g.fillRect(x, groundY, 40, 6);
        }
    }

    /** The player shooting at a walker, a shooter, and some coins: a little preview of the game. */
    private void drawScene(Graphics2D g, int groundY) {
        // Player
        g.setColor(new Color(90, 170, 255));
        g.fillRect(150, groundY - 36, 28, 36);
        g.setColor(new Color(20, 24, 40));
        g.fillRect(168, groundY - 27, 6, 6);

        // A bullet that keeps flying to the right
        int bulletX = 182 + (int) ((time * 320) % 300);
        g.setColor(new Color(255, 214, 90));
        g.fillRect(bulletX, groundY - 20, 12, 4);

        // Coins bobbing up and down
        for (int i = 0; i < 4; i++) {
            int bob = (int) (Math.sin(time * 4 + i) * 3);
            g.setColor(new Color(244, 197, 66));
            g.fillOval(660 + i * 34, groundY - 64 + bob, 16, 16);
            g.setColor(new Color(255, 236, 150));
            g.fillOval(664 + i * 34, groundY - 61 + bob, 5, 5);
        }

        // A walker pacing back and forth
        double swing = Math.sin(time * 1.3);
        int walkerX = 700 + (int) (swing * 60);
        int look = Math.cos(time * 1.3) > 0 ? 4 : 0;
        g.setColor(ACCENT);
        g.fillRoundRect(walkerX, groundY - 30, 30, 30, 10, 10);
        g.setColor(new Color(22, 26, 43));
        g.fillRect(walkerX + 6 + look, groundY - 22, 5, 5);
        g.fillRect(walkerX + 16 + look, groundY - 22, 5, 5);

        // A shooter guarding the right side
        g.setColor(new Color(176, 112, 255));
        g.fillRoundRect(850, groundY - 40, 32, 40, 8, 8);
        g.setColor(new Color(90, 60, 140));
        g.fillRect(840, groundY - 27, 12, 8);
        g.setColor(new Color(22, 26, 43));
        g.fillRect(856, groundY - 34, 7, 5);
    }

    /** The big two-colour title with a drop shadow. */
    private static class TitleLabel extends JComponent {
        private final Font font = new Font(Font.SANS_SERIF, Font.BOLD, 92);

        TitleLabel() {
            Dimension size = new Dimension(720, 112);
            setPreferredSize(size);
            setMaximumSize(size);
            setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(font);
            FontMetrics metrics = g2.getFontMetrics();
            String first = "BLAST";
            String second = "NITE";
            int x = (getWidth() - metrics.stringWidth(first + second)) / 2;
            int y = 92;
            int secondX = x + metrics.stringWidth(first);

            g2.setColor(new Color(0, 0, 0, 120));   // shadow
            g2.drawString(first + second, x + 5, y + 6);
            g2.setColor(TEXT);
            g2.drawString(first, x, y);
            g2.setColor(ACCENT);
            g2.drawString(second, secondX, y);
        }
    }

    /** A rounded button with a coloured stripe, a hover effect, and an optional second line. */
    private static class MenuButton extends JButton {
        private static final Color BODY = new Color(44, 50, 82);
        private static final Color BODY_HOVER = new Color(62, 72, 116);

        private final Color stripe;
        private String subtitle = "";
        private boolean hover;

        MenuButton(String text, Color stripe, Runnable action) {
            super(text);
            this.stripe = stripe;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            // Not focusable, so pressing Space in the game can't "click" a hidden button.
            setFocusable(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            Dimension size = new Dimension(320, 62);
            setPreferredSize(size);
            setMaximumSize(size);
            setMinimumSize(size);

            addActionListener(e -> action.run());
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth() - 6;    // leave room for the shadow
            int h = getHeight() - 6;
            int lift = hover ? -2 : 0; // the button rises a little under the mouse

            g2.setColor(new Color(0, 0, 0, 90));
            g2.fillRoundRect(4, 6, w, h, 18, 18);

            RoundRectangle2D shape = new RoundRectangle2D.Double(0, 2 + lift, w, h, 18, 18);
            g2.setColor(hover ? BODY_HOVER : BODY);
            g2.fill(shape);

            g2.clip(shape);                       // keep the stripe inside the rounded corners
            g2.setColor(stripe);
            g2.fillRect(0, 0, 8, getHeight());
            g2.setClip(null);

            if (hover) {
                g2.setColor(stripe);
                g2.setStroke(new BasicStroke(2f));
                g2.draw(shape);
            }

            FontMetrics big = g2.getFontMetrics(new Font(Font.SANS_SERIF, Font.BOLD, 22));
            g2.setFont(big.getFont());
            g2.setColor(TEXT);
            int textX = (w - big.stringWidth(getText())) / 2 + 4;
            if (subtitle.isEmpty()) {
                g2.drawString(getText(), textX, 2 + lift + h / 2 + big.getAscent() / 2 - 3);
            } else {
                g2.drawString(getText(), textX, 2 + lift + h / 2 - 2);
                FontMetrics small = g2.getFontMetrics(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                g2.setFont(small.getFont());
                g2.setColor(stripe);
                g2.drawString(subtitle, (w - small.stringWidth(subtitle)) / 2 + 4, 2 + lift + h / 2 + 18);
            }
            g2.dispose();
        }
    }
}