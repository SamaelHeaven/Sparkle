package sparkle.paints;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Color implements Paint {
    public static final Color BLACK = new Color(0, 0, 0, "BLACK");
    public static final Color WHITE = new Color(255, 255, 255, "WHITE");
    public static final Color GRAY = new Color(127, 127, 127, "GRAY");
    public static final Color RED = new Color(255, 0, 0, "RED");
    public static final Color GREEN = new Color(0, 255, 0, "GREEN");
    public static final Color BLUE = new Color(0, 0, 255, "BLUE");
    public static final Color YELLOW = new Color(255, 255, 0, "YELLOW");
    public static final Color ORANGE = new Color(255, 165, 0, "ORANGE");
    public static final Color PURPLE = new Color(127, 0, 127, "PURPLE");
    public static final Color TRANSPARENT = new Color(0, 0, 0, 0, "TRANSPARENT");
    private static final List<Color> colors = new ArrayList<>();

    static {
        colors.add(BLACK);
        colors.add(WHITE);
        colors.add(GRAY);
        colors.add(RED);
        colors.add(GREEN);
        colors.add(BLUE);
        colors.add(YELLOW);
        colors.add(ORANGE);
        colors.add(PURPLE);
        colors.add(TRANSPARENT);
    }

    private final java.awt.Color paint;
    private final String name;

    public Color(int red, int green, int blue) {
        this(red, green, blue, 255, null);
    }

    public Color(int red, int green, int blue, int alpha) {
        this(red, green, blue, alpha, null);
    }

    private Color(int red, int green, int blue, String name) {
        this(red, green, blue, 255, name);
    }

    private Color(int red, int green, int blue, int alpha, String name) {
        this.paint = new java.awt.Color(clampValue(red), clampValue(green), clampValue(blue), clampValue(alpha));
        this.name = Objects.requireNonNullElse(name, super.toString());
    }

    public Color(String hexadecimal) {
        name = super.toString();
        var color = hexadecimal;
        try {
            if (hexadecimal.startsWith("#")) {
                color = hexadecimal.substring(1);
            }
            var alpha = 255;
            if (color.length() == 8) {
                alpha = Integer.parseInt(color.substring(6, 8), 16);
            }
            var rgb = Integer.parseInt(color.substring(0, 6), 16);
            var red = rgb >> 16 & 255;
            var green = rgb >> 8 & 255;
            var blue = rgb & 255;
            paint = new java.awt.Color(clampValue(red), clampValue(green), clampValue(blue), clampValue(alpha));
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Invalid hexadecimal color string: " + hexadecimal);
        }
    }

    public static Color valueOf(String name) {
        for (var color : colors) {
            if (color.name.equals(name)) {
                return color;
            }
        }
        return null;
    }

    public static Color[] values() {
        return colors.toArray(new Color[0]);
    }

    @Override
    public java.awt.Paint getPaint() {
        return paint;
    }

    public int getRed() {
        return paint.getRed();
    }

    public int getGreen() {
        return paint.getGreen();
    }

    public int getBlue() {
        return paint.getBlue();
    }

    public int getAlpha() {
        return paint.getAlpha();
    }

    @Override
    public String toString() {
        return "{ \"red\": " + getRed() + " \"green\": " + getGreen() + " \"blue\": " + getBlue() + " \"alpha\": " + getAlpha() + " }";
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Color color) {
            return getRed() == color.getRed() && getGreen() == color.getGreen() && getBlue() == color.getBlue() && getAlpha() == color.getAlpha();
        }
        return super.equals(object);
    }

    private int clampValue(int value) {
        return Math.max(0, Math.min(255, value));
    }
}