package sparkle.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public final class KeyInput implements KeyListener {
    private static KeyInput instance = null;
    private final Set<Key> newKeysPressed;
    private final Set<Key> newKeysReleased;
    private final Set<Key> keysJustPressed;
    private final Set<Key> keysPressed;
    private final Set<Key> keysReleased;
    private String typedString;
    private String newTypedString;

    private KeyInput() {
        newKeysPressed = new ConcurrentSkipListSet<>();
        newKeysReleased = new ConcurrentSkipListSet<>();
        keysPressed = new HashSet<>();
        keysReleased = new HashSet<>();
        keysJustPressed = new HashSet<>();
        typedString = "";
        newTypedString = "";
    }

    public static boolean isKeyJustPressed(Key key) {
        return getInstance().keysJustPressed.contains(key);
    }

    public static boolean isKeyPressed(Key key) {
        return getInstance().keysPressed.contains(key);
    }

    public static boolean isKeyReleased(Key key) {
        return getInstance().keysReleased.contains(key);
    }

    public static Set<Key> getKeysJustPressed() {
        return Collections.unmodifiableSet(getInstance().keysJustPressed);
    }

    public static Set<Key> getKeysPressed() {
        return Collections.unmodifiableSet(getInstance().keysPressed);
    }

    public static Set<Key> getKeysReleased() {
        return Collections.unmodifiableSet(getInstance().keysReleased);
    }

    public static String getTypedString() {
        return getInstance().typedString;
    }

    static KeyInput getInstance() {
        Game.throwIfUninitialized();
        return instance = instance == null ? new KeyInput() : instance;
    }

    @Override
    public void keyTyped(KeyEvent event) {
        if (isCharTypedValid(event.getKeyChar())) {
            newTypedString += event.getKeyChar();
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        newKeysPressed.add(Key.getByEvent(event));
    }

    @Override
    public void keyReleased(KeyEvent event) {
        newKeysReleased.add(Key.getByEvent(event));
    }

    public void update() {
        updateKeysJustPressed();
        updateKeysPressed();
        updateKeysReleased();
        updateTypedString();
        handleFocusState();
    }

    public void reset() {
        keysPressed.clear();
    }

    private void updateTypedString() {
        typedString = newTypedString;
        newTypedString = "";
    }

    private void updateKeysJustPressed() {
        keysJustPressed.clear();
        keysJustPressed.addAll(newKeysPressed);
        keysJustPressed.removeAll(keysPressed);
    }

    private void updateKeysPressed() {
        keysPressed.addAll(newKeysPressed);
        newKeysPressed.clear();
    }

    private void updateKeysReleased() {
        keysReleased.clear();
        keysReleased.addAll(newKeysReleased);
        keysPressed.removeAll(newKeysReleased);
        newKeysReleased.clear();
    }

    private void handleFocusState() {
        if (FocusHandler.isFocused()) {
            return;
        }
        reset();
    }

    private boolean isCharTypedValid(char charTyped) {
        return (charTyped >= ' ' && charTyped <= '~');
    }
}