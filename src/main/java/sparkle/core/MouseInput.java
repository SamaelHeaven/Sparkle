package sparkle.core;

import sparkle.math.Vector2;
import sparkle.math.Vector2c;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public final class MouseInput extends MouseAdapter {
    private static MouseInput instance = null;
    private final Set<MouseButton> newMouseButtonsClicked;
    private final Set<MouseButton> newMouseButtonsPressed;
    private final Set<MouseButton> newMouseButtonsReleased;
    private final Set<MouseButton> mouseButtonsClicked;
    private final Set<MouseButton> mouseButtonsPressed;
    private final Set<MouseButton> mouseButtonsReleased;
    private final Vector2 mousePosition;
    private final Vector2 newMousePosition;
    private int scroll;
    private int newScroll;

    private MouseInput() {
        newMouseButtonsClicked = new ConcurrentSkipListSet<>();
        newMouseButtonsPressed = new ConcurrentSkipListSet<>();
        newMouseButtonsReleased = new ConcurrentSkipListSet<>();
        mouseButtonsClicked = new HashSet<>();
        mouseButtonsPressed = new HashSet<>();
        mouseButtonsReleased = new HashSet<>();
        mousePosition = new Vector2();
        newMousePosition = new Vector2();
    }

    public static boolean isMouseClicked(MouseButton mouseButton) {
        return getInstance().mouseButtonsClicked.contains(mouseButton);
    }

    public static boolean isMousePressed(MouseButton mouseButton) {
        return getInstance().mouseButtonsPressed.contains(mouseButton);
    }

    public static boolean isMouseReleased(MouseButton mouseButton) {
        return getInstance().mouseButtonsReleased.contains(mouseButton);
    }

    public static Set<MouseButton> getMouseButtonsClicked() {
        return Collections.unmodifiableSet(getInstance().mouseButtonsClicked);
    }

    public static Set<MouseButton> getMouseButtonsPressed() {
        return Collections.unmodifiableSet(getInstance().mouseButtonsPressed);
    }

    public static Set<MouseButton> getMouseButtonsReleased() {
        return Collections.unmodifiableSet(getInstance().mouseButtonsReleased);
    }

    public static Vector2c getMousePosition() {
        return new Vector2c(getInstance().mousePosition);
    }

    public static int getScroll() {
        return getInstance().scroll;
    }

    static MouseInput getInstance() {
        Game.throwIfUninitialized();
        return instance = instance == null ? new MouseInput() : instance;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        var mouseButton = MouseButton.getByEvent(event);
        newMouseButtonsClicked.add(mouseButton);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        var mouseButton = MouseButton.getByEvent(event);
        newMouseButtonsPressed.add(mouseButton);
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        var mouseButton = MouseButton.getByEvent(event);
        newMouseButtonsReleased.add(mouseButton);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        newScroll += e.getWheelRotation();
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        onMouseMoved(event);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        onMouseMoved(event);
    }

    public void update() {
        updateMouseButtonsClicked();
        updateMouseButtonsPressed();
        updateMouseButtonsReleased();
        updateMousePosition();
        updateScroll();
        handleFocusState();
    }

    public void reset() {
        mouseButtonsPressed.clear();
        scroll = 0;
    }

    private void updateMouseButtonsClicked() {
        mouseButtonsClicked.clear();
        mouseButtonsClicked.addAll(newMouseButtonsClicked);
        newMouseButtonsClicked.clear();
    }

    private void updateMouseButtonsPressed() {
        mouseButtonsPressed.addAll(newMouseButtonsPressed);
        newMouseButtonsPressed.clear();
    }

    private void updateMouseButtonsReleased() {
        mouseButtonsReleased.clear();
        mouseButtonsReleased.addAll(newMouseButtonsReleased);
        mouseButtonsPressed.removeAll(newMouseButtonsReleased);
        newMouseButtonsReleased.clear();
    }

    private void updateMousePosition() {
        var scaleFactor = Game.getScaleFactor();
        var windowSize = Game.getSize().multiply(scaleFactor);
        var offset = Game.getActualSize().minus(windowSize).divide(2);
        var position = newMousePosition.minus(offset).divide(scaleFactor);
        mousePosition.set(position.clampX(0, Game.getWidth()).clampY(0, Game.getHeight()));
    }

    private void updateScroll() {
        scroll = newScroll;
        newScroll = 0;
    }

    private void handleFocusState() {
        if (FocusHandler.isFocused()) {
            return;
        }
        reset();
    }

    private void onMouseMoved(MouseEvent event) {
        newMousePosition.set(event.getX(), event.getY());
    }
}