package blastnite.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import blastnite.core.GameWorld;

/**
 * The start screen: the title and four buttons (Easy, Difficult, How to Play, Quit).
 * It does not know what the buttons do; the window passes that in.
 */
public class MenuPanel extends JPanel {
    private static final Color BACKGROUND = new Color(24, 26, 32);
    private static final Color BUTTON = new Color(58, 66, 98);
    private static final Color BUTTON_HOVER = new Color(79, 163, 255);
    private static final Color TEXT = new Color(235, 238, 248);
    private static final Color ACCENT = new Color(255, 154, 60);

    public MenuPanel(Runnable onEasy, Runnable onDifficult, Runnable onHowToPlay, Runnable onQuit) {
        setPreferredSize(new Dimension(GameWorld.WIDTH, GameWorld.HEIGHT));
        setBackground(BACKGROUND);
        setLayout(new GridBagLayout());   // centers the column of content

        JPanel column = new JPanel();
        column.setOpaque(false);
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("BLASTNITE");
        title.setFont(new Font(Font.MONOSPACED, Font.BOLD, 84));
        title.setForeground(TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Run. Jump. Blast your way to the flag.");
        subtitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        subtitle.setForeground(ACCENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        column.add(title);
        column.add(Box.createVerticalStrut(8));
        column.add(subtitle);
        column.add(Box.createVerticalStrut(40));
        column.add(makeButton("Play: Easy", onEasy));
        column.add(Box.createVerticalStrut(14));
        column.add(makeButton("Play: Difficult", onDifficult));
        column.add(Box.createVerticalStrut(14));
        column.add(makeButton("How to Play", onHowToPlay));
        column.add(Box.createVerticalStrut(14));
        column.add(makeButton("Quit", onQuit));
        column.add(Box.createVerticalStrut(70));   // keeps the buttons above the ground drawing

        add(column);
    }

    private JButton makeButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        button.setForeground(TEXT);
        button.setBackground(BUTTON);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        // Not focusable, so pressing Space in the game can't "click" a hidden button.
        button.setFocusable(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        Dimension size = new Dimension(260, 54);
        button.setPreferredSize(size);
        button.setMaximumSize(size);

        button.addActionListener(e -> action.run());
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON);
            }
        });
        return button;
    }

    /** Draws a strip of ground with the player and an enemy, as decoration. */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int groundY = getHeight() - 80;
        for (int x = 0; x < getWidth(); x += 40) {
            g2.setColor(new Color(58, 66, 98));
            g2.fillRect(x, groundY, 40, 80);
            g2.setColor(new Color(46, 52, 78));
            g2.drawRect(x, groundY, 39, 39);
            g2.drawRect(x, groundY + 40, 39, 39);
            g2.setColor(new Color(96, 110, 156));
            g2.fillRect(x, groundY, 40, 6);
        }
        // the player
        g2.setColor(new Color(90, 170, 255));
        g2.fillRect(180, groundY - 36, 28, 36);
        g2.setColor(new Color(20, 24, 40));
        g2.fillRect(198, groundY - 27, 6, 6);
        // a bullet
        g2.setColor(new Color(255, 214, 90));
        g2.fillRect(250, groundY - 20, 12, 4);
        // a walker
        g2.setColor(new Color(255, 154, 60));
        g2.fillRoundRect(720, groundY - 30, 30, 30, 10, 10);
        g2.setColor(new Color(22, 26, 43));
        g2.fillRect(726, groundY - 22, 5, 5);
        g2.fillRect(736, groundY - 22, 5, 5);
    }
}