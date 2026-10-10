package blastnite.input;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;


public class InputHandler {
    private final Set<Integer> heldKeys = new HashSet<>();
    private final InputMap inputMap;
    private final ActionMap actionMap;

    public InputHandler(JComponent component) {
        inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        actionMap = component.getActionMap();

        bindKey(KeyEvent.VK_W);
        bindKey(KeyEvent.VK_A);
        bindKey(KeyEvent.VK_S);
        bindKey(KeyEvent.VK_D);
        bindKey(KeyEvent.VK_SPACE);
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

  
    public int getMoveX() {
        return (isHeld(KeyEvent.VK_D) ? 1 : 0) - (isHeld(KeyEvent.VK_A) ? 1 : 0);
    }

       
    public boolean isJumpHeld() {
        return isHeld(KeyEvent.VK_SPACE);
    }
}
