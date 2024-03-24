package sparkle.drawables;

import sparkle.math.Vector2;
import sparkle.paints.Color;
import sparkle.paints.Paint;

import java.awt.*;

public final class Oval extends BasicShape {
    public Oval() {
        super();
    }

    public Oval(Vector2 position, Vector2 size, Paint fill) {
        super(position, size, fill);
    }

    public Oval(Vector2 position, Vector2 size, Paint stroke, float strokeWidth) {
        super(position, size, stroke, strokeWidth);
    }

    public Oval(Vector2 position, Vector2 size, Paint fill, Paint stroke, StrokeType strokeType, float strokeWidth, float rotation) {
        super(position, size, fill, stroke, strokeType, strokeWidth, rotation);
    }

    @Override
    protected void render(Graphics2D graphics) {
        var fillIsTransparent = super.getFill().equals(Color.TRANSPARENT);
        var strokeIsTransparent = super.getStroke().equals(Color.TRANSPARENT) || super.getStrokeWidth() == 0;
        if (super.isOutsideScreen() || (fillIsTransparent && strokeIsTransparent)) {
            return;
        }
        var x = super.getRenderingMode().getModifierX(super.getAnchor().getX() + super.getPosition().getX());
        var y = super.getRenderingMode().getModifierY(super.getAnchor().getY() + super.getPosition().getY());
        var oldTransform = graphics.getTransform();
        if (super.getRotation() != 0) {
            graphics.rotate(Math.toRadians(super.getRotation()), x + (super.getSize().getX() / 2.0), y + super.getSize().getY() / 2.0);
        }
        render(graphics, x, y);
        graphics.setTransform(oldTransform);
    }

    @Override
    protected void render(Graphics2D graphics, float x, float y) {
        var fillIsTransparent = super.getFill().equals(Color.TRANSPARENT);
        var strokeIsTransparent = super.getStroke().equals(Color.TRANSPARENT) || super.getStrokeWidth() == 0;
        if (!fillIsTransparent) {
            graphics.setPaint(super.getFill().getPaint());
            graphics.fillOval(Math.round(x), Math.round(y), Math.round(super.getSize().getX()), Math.round(super.getSize().getY()));
        }
        if (!strokeIsTransparent) {
            graphics.setPaint(super.getStroke().getPaint());
            graphics.setStroke(super.strokeObject);
            graphics.drawOval(Math.round(x), Math.round(y), Math.round(super.getSize().getX()), Math.round(super.getSize().getY()));
        }
    }

    @Override
    protected StrokeType getDefaultStrokeType() {
        return StrokeType.ROUNDED;
    }
}