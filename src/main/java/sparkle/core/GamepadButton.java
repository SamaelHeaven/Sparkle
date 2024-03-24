package sparkle.core;

import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public enum GamepadButton {
    A(GLFW.GLFW_GAMEPAD_BUTTON_A),
    B(GLFW.GLFW_GAMEPAD_BUTTON_B),
    X(GLFW.GLFW_GAMEPAD_BUTTON_X),
    Y(GLFW.GLFW_GAMEPAD_BUTTON_Y),
    LEFT_BUMPER(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER),
    RIGHT_BUMPER(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER),
    BACK(GLFW.GLFW_GAMEPAD_BUTTON_BACK),
    START(GLFW.GLFW_GAMEPAD_BUTTON_START),
    GUIDE(GLFW.GLFW_GAMEPAD_BUTTON_GUIDE),
    LEFT_THUMB(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB),
    RIGHT_THUMB(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB),
    DPAD_UP(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP),
    DPAD_RIGHT(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT),
    DPAD_DOWN(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN),
    DPAD_LEFT(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT);

    private static final Map<Integer, GamepadButton> buttons = new HashMap<>();

    static {
        for (var button : values()) {
            buttons.put(button.code, button);
        }
    }

    private final int code;

    GamepadButton(int code) {
        this.code = code;
    }

    static GamepadButton getByCode(int code) {
        return buttons.get(code);
    }
}