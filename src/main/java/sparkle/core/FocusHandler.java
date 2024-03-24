package sparkle.core;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public final class FocusHandler implements FocusListener {
    private static FocusHandler instance = null;
    private boolean focused;

    private FocusHandler() {
        focused = true;
    }

    public static boolean isFocused() {
        return getInstance().focused;
    }

    static FocusHandler getInstance() {
        Game.throwIfUninitialized();
        return instance = instance == null ? new FocusHandler() : instance;
    }

    @Override
    public void focusGained(FocusEvent e) {
        focused = true;
    }

    @Override
    public void focusLost(FocusEvent e) {
        focused = false;
    }
}