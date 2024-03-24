package sparkle.drawables;

import sparkle.core.Drawable;
import sparkle.core.Game;
import sparkle.math.Vector2;
import sparkle.paints.Color;
import sparkle.paints.Paint;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Objects;

public final class Line extends Drawable {
    private static final int DEFAULT_STROKE_WIDTH = 1;
    private final Vector2 startPosition;
    private final Vector2 endPosition;
    private Paint stroke;
    private StrokeType strokeType;
    private Stroke strokeObject;
    private float strokeWidth;

    public Line() {
        this(null, null, Color.TRANSPARENT, null, DEFAULT_STROKE_WIDTH);
    }

    public Line(Vector2 startPosition, Vector2 endPosition, Paint stroke) {
        this(startPosition, endPosition, stroke, null, DEFAULT_STROKE_WIDTH);
    }

    public Line(Vector2 startPosition, Vector2 endPosition, Paint stroke, StrokeType strokeType, float strokeWidth) {
        this.startPosition = Objects.requireNonNullElse(startPosition, new Vector2());
        this.endPosition = Objects.requireNonNullElse(endPosition, new Vector2());
        this.stroke = Objects.requireNonNullElse(stroke, Color.TRANSPARENT);
        this.strokeType = Objects.requireNonNullElse(strokeType, StrokeType.SQUARED);
        this.strokeWidth = Math.max(0, strokeWidth);
        strokeObject = this.strokeType.build(this.strokeWidth);
    }

    @Override
    public boolean isOutsideScreen() {
        var startX = super.getRenderingMode().getModifierX(super.getAnchor().getX() + startPosition.getX());
        var startY = super.getRenderingMode().getModifierY(super.getAnchor().getY() + startPosition.getY());
        var endX = super.getRenderingMode().getModifierX(super.getAnchor().getX() + endPosition.getX());
        var endY = super.getRenderingMode().getModifierY(super.getAnchor().getY() + endPosition.getY());
        var line = new Line2D.Float(startX, startY, endX, endY);
        return !strokeObject.createStrokedShape(line).intersects(0, 0, Game.getWidth(), Game.getHeight());
    }

    @Override
    protected void render(Graphics2D graphics) {
        if (isOutsideScreen() || stroke.equals(Color.TRANSPARENT) || strokeWidth == 0) {
            return;
        }
        var startX = super.getRenderingMode().getModifierX(super.getAnchor().getX() + startPosition.getX());
        var startY = super.getRenderingMode().getModifierY(super.getAnchor().getY() + startPosition.getY());
        var endX = super.getRenderingMode().getModifierX(super.getAnchor().getX() + endPosition.getX());
        var endY = super.getRenderingMode().getModifierY(super.getAnchor().getY() + endPosition.getY());
        graphics.setPaint(stroke.getPaint());
        graphics.setStroke(strokeObject);
        graphics.drawLine(Math.round(startX), Math.round(startY), Math.round(endX), Math.round(endY));
    }

    public Vector2 getStartPosition() {
        return startPosition;
    }

    public Vector2 getEndPosition() {
        return endPosition;
    }

    public Paint getStroke() {
        return stroke;
    }

    public void setStroke(Paint stroke) {
        this.stroke = Objects.requireNonNullElse(stroke, Color.TRANSPARENT);
    }

    public StrokeType getStrokeType() {
        return strokeType;
    }

    public void setStrokeType(StrokeType strokeType) {
        this.strokeType = Objects.requireNonNullElse(strokeType, StrokeType.SQUARED);
        strokeObject = this.strokeType.build(strokeWidth);
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = Math.max(0, strokeWidth);
        strokeObject = strokeType.build(this.strokeWidth);
    }
}