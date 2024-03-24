package sparkle.core;

import javax.swing.*;
import java.awt.event.MouseEvent;

public enum MouseButton {
    PRIMARY, MIDDLE, SECONDARY;

    static MouseButton getByEvent(MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event)) {
            return PRIMARY;
        } else if (SwingUtilities.isMiddleMouseButton(event)) {
            return MIDDLE;
        } else if (SwingUtilities.isRightMouseButton(event)) {
            return SECONDARY;
        }
        return null;
    }
}