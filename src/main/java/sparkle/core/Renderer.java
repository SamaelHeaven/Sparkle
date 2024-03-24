package sparkle.core;

import sparkle.paints.Paint;

import java.awt.*;
import java.util.Objects;

public final class Renderer {
    private Graphics2D graphics;
    private Interpolation interpolation;

    Renderer() {
        interpolation = Interpolation.NEAREST_NEIGHBOR;
    }

    public void clearScreen(Paint paint) {
        graphics.setPaint(paint.getPaint());
        graphics.fillRect(0, 0, Game.getWidth(), Game.getHeight());
    }

    public void draw(Drawable drawable) {
        drawable.render(graphics);
    }

    public Interpolation getInterpolation() {
        return interpolation;
    }

    public void setInterpolation(Interpolation interpolation) {
        this.interpolation = Objects.requireNonNullElse(interpolation, Interpolation.NEAREST_NEIGHBOR);
        if (graphics != null) {
            graphics.setRenderingHint(Interpolation.key, this.interpolation.value);
        }
    }

    void setGraphics(Graphics2D graphics) {
        this.graphics = graphics;
        setInterpolation(interpolation);
    }
}