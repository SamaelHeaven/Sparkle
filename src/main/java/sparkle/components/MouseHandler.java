package sparkle.components;

import sparkle.core.Component;
import sparkle.core.MouseButton;
import sparkle.core.MouseInput;
import sparkle.math.Vector2Base;
import sparkle.math.Vector2c;

import java.util.List;
import java.util.Objects;

public final class MouseHandler extends Component {
    private static final MouseButton[] MOUSE_BUTTONS = MouseButton.values();
    private Vector2Base offset;
    private boolean mouseInside;

    public MouseHandler() {
        this(null);
    }

    public MouseHandler(Vector2Base offset) {
        this.offset = Objects.requireNonNullElse(offset, new Vector2c());
    }

    @Override
    protected void update() {
        var mouseListeners = super.getComponents(MouseListener.class);
        updateMouseMovementCallbacks(mouseListeners);
        updateMouseButtonCallbacks(mouseListeners);
    }

    public Vector2Base getOffset() {
        return offset;
    }

    public void setOffset(Vector2Base offset) {
        this.offset = Objects.requireNonNullElse(offset, new Vector2c());
    }

    private void updateMouseMovementCallbacks(List<MouseListener> mouseListeners) {
        var mouseInside = isMouseInside();
        var mouseEnter = !this.mouseInside && mouseInside;
        var mouseLeave = this.mouseInside && !isMouseInside();
        for (var mouseListener : mouseListeners) {
            if (mouseEnter) {
                mouseListener.onMouseEnter();
            }
            if (mouseInside) {
                mouseListener.onMouseInside();
            }
            if (mouseLeave) {
                mouseListener.onMouseLeave();
            }
        }
        this.mouseInside = mouseInside;
    }

    private void updateMouseButtonCallbacks(List<MouseListener> mouseListeners) {
        if (!mouseInside) {
            return;
        }
        for (var mouseListener : mouseListeners) {
            for (var mouseButton : MOUSE_BUTTONS) {
                if (MouseInput.isMouseClicked(mouseButton)) {
                    mouseListener.onMouseButtonClicked(mouseButton);
                }
                if (MouseInput.isMousePressed(mouseButton)) {
                    mouseListener.onMouseButtonPressed(mouseButton);
                }
                if (MouseInput.isMouseReleased(mouseButton)) {
                    mouseListener.onMouseButtonReleased(mouseButton);
                }
            }
        }
    }

    private boolean isMouseInside() {
        return super.getGameObject().getBounds().contains(MouseInput.getMousePosition().plus(offset));
    }
}