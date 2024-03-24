package sparkle.core;

import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public enum GamepadAxis {
    LEFT_X(GLFW.GLFW_GAMEPAD_AXIS_LEFT_X),
    LEFT_Y(GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y),
    RIGHT_X(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X),
    RIGHT_Y(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y),
    LEFT_TRIGGER(GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER),
    RIGHT_TRIGGER(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER);

    private static final Map<Integer, GamepadAxis> axes = new HashMap<>();

    static {
        for (var axis : values()) {
            axes.put(axis.code, axis);
        }
    }

    private final int code;

    GamepadAxis(int code) {
        this.code = code;
    }

    static GamepadAxis getByCode(int code) {
        return axes.get(code);
    }
}