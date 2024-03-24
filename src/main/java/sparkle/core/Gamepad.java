package sparkle.core;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;
import org.lwjgl.system.Configuration;

import java.util.*;

public final class Gamepad {
    private static final GamepadAxis[] AXES = GamepadAxis.values();
    private static final List<Gamepad> gamepads = new ArrayList<>();

    static {
        Game.throwIfUninitialized();
        initializeGLFW();
        initializeGamepads();
    }

    private final Set<GamepadButton> buttonsPressed;
    private final Set<GamepadButton> buttonsReleased;
    private final Set<GamepadButton> buttonsJustPressed;
    private final Set<GamepadButton> lastButtonsPressed;
    private final Map<GamepadAxis, Float> axes;
    private final int id;

    private Gamepad(int id) {
        buttonsPressed = new HashSet<>();
        buttonsReleased = new HashSet<>();
        buttonsJustPressed = new HashSet<>();
        lastButtonsPressed = new HashSet<>();
        axes = new HashMap<>();
        for (var axis : GamepadAxis.values()) {
            axes.put(axis, 0f);
        }
        this.id = id;
    }

    public static List<Gamepad> getGamepads() {
        return Collections.unmodifiableList(gamepads);
    }

    static void updateAll() {
        for (var gamepad : gamepads) {
            gamepad.update();
        }
    }

    private static void initializeGLFW() {
        Configuration.GLFW_CHECK_THREAD0.set(false);
        if (!GLFW.glfwInit()) {
            throw new RuntimeException("Could not initialize GLFW");
        }
        Runtime.getRuntime().addShutdownHook(new Thread(GLFW::glfwTerminate));
    }

    private static void initializeGamepads() {
        for (var i = GLFW.GLFW_JOYSTICK_1; i <= GLFW.GLFW_JOYSTICK_LAST; i++) {
            gamepads.add(new Gamepad(i));
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return GLFW.glfwGetGamepadName(id);
    }

    public boolean isButtonPressed(GamepadButton button) {
        return buttonsPressed.contains(button);
    }

    public boolean isButtonReleased(GamepadButton button) {
        return buttonsReleased.contains(button);
    }

    public boolean isButtonJustPressed(GamepadButton button) {
        return buttonsJustPressed.contains(button);
    }

    public Set<GamepadButton> getButtonsPressed() {
        return Collections.unmodifiableSet(buttonsPressed);
    }

    public Set<GamepadButton> getButtonsReleased() {
        return Collections.unmodifiableSet(buttonsReleased);
    }

    public Set<GamepadButton> getButtonsJustPressed() {
        return Collections.unmodifiableSet(buttonsJustPressed);
    }

    public float getAxis(GamepadAxis gamepadAxis) {
        return axes.get(gamepadAxis);
    }

    public boolean isConnected() {
        return GLFW.glfwJoystickPresent(id) && GLFW.glfwJoystickIsGamepad(id);
    }

    private void update() {
        if (!isConnected() || !FocusHandler.isFocused()) {
            reset();
            return;
        }
        try (var state = GLFWGamepadState.malloc()) {
            GLFW.glfwGetGamepadState(id, state);
            updateButtonsPressed(state);
            updateAxes(state);
        }
        updateButtonsReleased();
        updateButtonsJustPressed();
        updateLastButtonsPressed();
    }

    private void updateButtonsPressed(GLFWGamepadState state) {
        buttonsPressed.clear();
        for (var i = 0; i < state.buttons().limit(); i++) {
            if (state.buttons(i) != 0) {
                buttonsPressed.add(GamepadButton.getByCode(i));
            }
        }
    }

    private void updateAxes(GLFWGamepadState state) {
        for (var i = 0; i < state.axes().limit(); i++) {
            axes.put(GamepadAxis.getByCode(i), state.axes(i));
        }
    }

    private void updateButtonsReleased() {
        buttonsReleased.clear();
        for (var button : lastButtonsPressed) {
            if (!buttonsPressed.contains(button)) {
                buttonsReleased.add(button);
            }
        }
    }

    private void updateButtonsJustPressed() {
        buttonsJustPressed.clear();
        for (var button : buttonsPressed) {
            if (!lastButtonsPressed.contains(button)) {
                buttonsJustPressed.add(button);
            }
        }
    }

    private void updateLastButtonsPressed() {
        lastButtonsPressed.clear();
        lastButtonsPressed.addAll(buttonsPressed);
    }

    private void reset() {
        buttonsPressed.clear();
        buttonsReleased.clear();
        buttonsJustPressed.clear();
        lastButtonsPressed.clear();
        for (var axis : AXES) {
            if (axes.get(axis) != 0) {
                axes.put(axis, 0f);
            }
        }
    }
}