package sparkle.core;

import sparkle.math.Vector2c;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public final class Game {
    private static Game instance = null;
    private GameConfig gameConfig;
    private RenderingEngine renderingEngine;
    private FPSTarget fpsTarget;
    private Time time;
    private KeyInput keyInput;
    private MouseInput mouseInput;
    private JFrame frame;
    private JPanel panel;
    private Scene scene;
    private Cursor cursor;
    private DisplayMode displayMode;
    private Renderer renderer;
    private boolean running;
    private boolean cleanup;

    private Game() {}

    public static void run(GameConfig gameConfig, Scene scene) {
        if (instance != null) {
            throw new IllegalStateException("Game has already been initialized");
        }
        var instance = getInstance();
        setScene(scene);
        instance.running = true;
        instance.gameConfig = copyConfig(gameConfig);
        if (instance.gameConfig.isHardwareAccelerated()) {
            System.setProperty("sun.java2d.opengl", "true");
        }
        instance.fpsTarget = instance.gameConfig.getFPSTarget();
        instance.initialize();
        instance.loop();
    }

    public static void stop() {
        throwIfUninitialized();
        getInstance().running = false;
    }

    public static Renderer getRenderer() {
        throwIfUninitialized();
        return getInstance().renderer;
    }

    public static Scene getScene() {
        throwIfUninitialized();
        return getInstance().scene;
    }

    public static void setScene(Scene scene) {
        throwIfUninitialized();
        var instance = getInstance();
        instance.scene = Objects.requireNonNull(scene);
        if (instance.running) {
            instance.scene.start();
            instance.cleanup = true;
        }
    }

    public static Cursor getCursor() {
        throwIfUninitialized();
        return getInstance().cursor;
    }

    public static void setCursor(Cursor cursor) {
        throwIfUninitialized();
        var instance = getInstance();
        var newCursor = Objects.requireNonNullElse(cursor, Cursor.DEFAULT);
        instance.cursor = newCursor;
        instance.frame.setCursor(newCursor.getCursor());
    }

    public static DisplayMode getDisplayMode() {
        throwIfUninitialized();
        return getInstance().displayMode;
    }

    public static void setDisplayMode(DisplayMode displayMode) {
        throwIfUninitialized();
        var instance = getInstance();
        var newDisplayMode = Objects.requireNonNullElse(displayMode, DisplayMode.WINDOW);
        newDisplayMode = (newDisplayMode == DisplayMode.FULLSCREEN && !DisplayMode.isFullscreenSupported() ? DisplayMode.MAXIMIZED : newDisplayMode);
        if (instance.displayMode == newDisplayMode) {
            return;
        }
        instance.displayMode = newDisplayMode;
        try {
            instance.displayMode.enable();
        } catch (RuntimeException e) {
            System.exit(1);
        }
        instance.keyInput.reset();
        instance.mouseInput.reset();
        instance.panel.requestFocusInWindow();
    }

    public static int getWidth() {
        throwIfUninitialized();
        return getInstance().gameConfig.getWidth();
    }

    public static int getHeight() {
        throwIfUninitialized();
        return getInstance().gameConfig.getHeight();
    }

    public static Vector2c getSize() {
        throwIfUninitialized();
        return new Vector2c(getWidth(), getHeight());
    }

    public static FPSTarget getFPSTarget() {
        throwIfUninitialized();
        return getInstance().fpsTarget;
    }

    public static void setFPSTarget(FPSTarget fpsTarget) {
        throwIfUninitialized();
        getInstance().fpsTarget = Objects.requireNonNullElse(fpsTarget, FPSTarget.FPS_60);
    }

    static JFrame getFrame() {
        throwIfUninitialized();
        return getInstance().frame;
    }

    static GraphicsDevice getDevice() {
        throwIfUninitialized();
        return getGraphicsConfiguration().getDevice();
    }

    static GraphicsConfiguration getGraphicsConfiguration() {
        throwIfUninitialized();
        var graphicsConfiguration = getInstance().frame.getGraphicsConfiguration();
        return graphicsConfiguration == null ? GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration() : graphicsConfiguration;
    }

    static Vector2c getActualSize() {
        throwIfUninitialized();
        var instance = getInstance();
        var bounds = instance.frame.getBounds();
        return new Vector2c(bounds.width, bounds.height);
    }

    static float getScaleFactor() {
        throwIfUninitialized();
        var actualSize = getActualSize();
        return Math.min(actualSize.getX() / getWidth(), actualSize.getY() / getHeight());
    }

    static void throwIfUninitialized() {
        if (instance == null) {
            throw new IllegalStateException("Game has not been initialized");
        }
    }

    private static Game getInstance() {
        return instance = instance == null ? new Game() : instance;
    }

    private static GameConfig copyConfig(GameConfig gameConfig) {
        var result = new GameConfig();
        if (gameConfig == null) {
            return result;
        }
        result.setIcon(gameConfig.getIcon());
        result.setTitle(gameConfig.getTitle());
        result.setFPSTarget(gameConfig.getFPSTarget());
        result.setDisplayMode(gameConfig.getDisplayMode());
        result.setCursor(gameConfig.getCursor());
        result.setWidth(gameConfig.getWidth());
        result.setHeight(gameConfig.getHeight());
        return result;
    }

    private void initialize() {
        initializeTaskbar();
        initializeInput();
        initializeFrame();
        initializePanel();
        initializeRenderingEngine();
        initializeCursor();
        initializeTime();
    }

    private void initializeTaskbar() {
        var icon = getIcon();
        if (Taskbar.isTaskbarSupported() && icon != null) {
            var taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                taskbar.setIconImage(icon);
            }
        }
    }

    private void initializeInput() {
        keyInput = KeyInput.getInstance();
        mouseInput = MouseInput.getInstance();
        Gamepad.updateAll();
    }

    private void initializePanel() {
        panel = new JPanel();
        panel.setIgnoreRepaint(true);
        panel.setFocusable(true);
        panel.setFocusTraversalKeysEnabled(false);
        panel.addKeyListener(keyInput);
        panel.addMouseListener(mouseInput);
        panel.addMouseMotionListener(mouseInput);
        panel.addMouseWheelListener(mouseInput);
        panel.addFocusListener(FocusHandler.getInstance());
        frame.add(panel);
        setDisplayMode(gameConfig.getDisplayMode());
    }

    private void initializeFrame() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setIgnoreRepaint(true);
        frame.setUndecorated(true);
        frame.setTitle(gameConfig.getTitle());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setState(JFrame.NORMAL);
        frame.setIconImage(getIcon());
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop();
            }
        });
    }

    private Image getIcon() {
        return gameConfig.getIcon() == null ? null : gameConfig.getIcon().getImage();
    }

    private void initializeRenderingEngine() {
        renderer = new Renderer();
        renderingEngine = new RenderingEngine();
        renderingEngine.initialize();
    }

    private void initializeCursor() {
        setCursor(gameConfig.getCursor());
    }

    private void initializeTime() {
        time = Time.getInstance();
    }

    private void loop() {
        scene.start();
        while (running) {
            update();
        }
        dispose();
    }

    private void update() {
        time.update();
        keyInput.update();
        mouseInput.update();
        Gamepad.updateAll();
        if (cleanup) {
            System.gc();
            cleanup = false;
        }
        renderer.setGraphics(renderingEngine.buildGraphics());
        scene.update();
        renderingEngine.renderScreen();
    }

    private void dispose() {
        try {
            frame.setVisible(false);
            frame.dispose();
        } catch (RuntimeException e) {
            System.exit(1);
        }
    }
}