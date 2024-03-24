package sparkle.utils;

import sparkle.core.Gamepad;
import sparkle.core.GamepadAxis;
import sparkle.core.Key;
import sparkle.core.KeyInput;
import sparkle.math.Vector2c;

public enum InputAxis {
    HORIZONTAL {
        @Override
        public int get() {
            return get(Key.LEFT, Key.A, Key.RIGHT, Key.D, GamepadAxis.LEFT_X);
        }
    }, VERTICAL {
        @Override
        public int get() {
            return get(Key.UP, Key.W, Key.DOWN, Key.S, GamepadAxis.LEFT_Y);
        }
    };

    public static Vector2c getBoth() {
        return new Vector2c(HORIZONTAL.get(), VERTICAL.get());
    }

    public static int get(Key minusKey, Key minusKeyAlt, Key plusKey, Key plusKeyAlt, GamepadAxis gamepadAxis) {
        var axis = Math.round(Gamepad.getGamepads().getFirst().getAxis(gamepadAxis));
        var minus = KeyInput.isKeyPressed(minusKey) || KeyInput.isKeyPressed(minusKeyAlt) || axis == -1;
        var plus = KeyInput.isKeyPressed(plusKey) || KeyInput.isKeyPressed(plusKeyAlt) || axis == 1;
        if (minus && !plus) {
            return -1;
        }
        if (plus && !minus) {
            return 1;
        }
        return 0;
    }

    public abstract int get();
}