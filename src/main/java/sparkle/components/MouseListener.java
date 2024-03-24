package sparkle.components;

import sparkle.core.MouseButton;

public interface MouseListener {
    default void onMouseEnter() {}

    default void onMouseInside() {}

    default void onMouseLeave() {}

    default void onMouseButtonClicked(MouseButton mouseButton) {}

    default void onMouseButtonPressed(MouseButton mouseButton) {}

    default void onMouseButtonReleased(MouseButton mouseButton) {}
}