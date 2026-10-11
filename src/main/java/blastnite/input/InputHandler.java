package blastnite.input;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * Tracks which keys (and whether the left mouse button) are currently held down.
 *
 * Uses Swing Key Bindings instead of a KeyListener: a KeyListener only
 * receives events while its component has keyboard focus, which breaks
 * easily. WHEN_IN_FOCUSED_WINDOW works as long as the window is focused.
 */
public class InputHandler {
    private final Set<Integer> heldKeys = new HashSet<>();
    private final InputMap inputMap;
    private final ActionMap actionMap;
    private boolean mouseHeld;

    public InputHandler(JComponent component) {
        inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        actionMap = component.getActionMap();

        bindKey(KeyEvent.VK_A);
        bindKey(KeyEvent.VK_D);
        bindKey(KeyEvent.VK_SPACE);
        bindKey(KeyEvent.VK_R);

        // Shooting uses the left mouse button. Like the keys, we only remember
        // whether it is held; the game decides what to do with that each tick.
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    mouseHeld = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    mouseHeld = false;
                }
            }
        });
    }

    private void bindKey(int keyCode) {
        String pressedName = "pressed " + keyCode;
        String releasedName = "released " + keyCode;

        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, false), pressedName);
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, true), releasedName);

        actionMap.put(pressedName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                heldKeys.add(keyCode);
            }
        });
        actionMap.put(releasedName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                heldKeys.remove(keyCode);
            }
        });
    }

    public boolean isHeld(int keyCode) {
        return heldKeys.contains(keyCode);
    }

    /** -1 = left, 0 = none (or both), +1 = right. */
    public int getMoveX() {
        return (isHeld(KeyEvent.VK_D) ? 1 : 0) - (isHeld(KeyEvent.VK_A) ? 1 : 0);
    }

    /** True while the jump key (Space) is held. */
    public boolean isJumpHeld() {
        return isHeld(KeyEvent.VK_SPACE);
    }

    /** True while the left mouse button is held (shoot). */
    public boolean isShootHeld() {
        return mouseHeld;
    }

    /** True while the restart key (R) is held. */
    public boolean isRestartHeld() {
        return isHeld(KeyEvent.VK_R);
    }
}
