package sparkle.core;

import sparkle.assets.Texture;

import java.util.Objects;

public final class GameConfig {
    private static final int DEFAULT_WIDTH = 960;
    private static final int DEFAULT_HEIGHT = 540;
    private Texture icon;
    private String title;
    private FPSTarget fpsTarget;
    private DisplayMode displayMode;
    private Cursor cursor;
    private int width;
    private int height;
    private boolean hardwareAccelerated;

    public GameConfig() {
        this(null, null, null, null, Cursor.DEFAULT, DEFAULT_WIDTH, DEFAULT_HEIGHT, true);
    }

    public GameConfig(Texture icon, String title, FPSTarget fpsTarget, DisplayMode displayMode, Cursor cursor, int width, int height, boolean hardwareAccelerated) {
        setIcon(icon);
        setTitle(title);
        setFPSTarget(fpsTarget);
        setDisplayMode(displayMode);
        setCursor(cursor);
        setWidth(width);
        setHeight(height);
        setHardwareAccelerated(hardwareAccelerated);
    }

    public Texture getIcon() {
        return icon;
    }

    public void setIcon(Texture icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Objects.requireNonNullElse(title, "");
    }

    public FPSTarget getFPSTarget() {
        return fpsTarget;
    }

    public void setFPSTarget(FPSTarget fpsTarget) {
        this.fpsTarget = Objects.requireNonNullElse(fpsTarget, FPSTarget.FPS_60);
    }

    public DisplayMode getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = Objects.requireNonNullElse(displayMode, DisplayMode.WINDOW);
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = Objects.requireNonNullElse(cursor, Cursor.DEFAULT);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be bigger than zero");
        }
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be bigger than 0");
        }
        this.height = height;
    }

    public boolean isHardwareAccelerated() {
        return hardwareAccelerated;
    }

    public void setHardwareAccelerated(boolean hardwareAccelerated) {
        this.hardwareAccelerated = hardwareAccelerated;
    }
}