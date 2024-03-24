package sparkle.drawables;

import sparkle.core.Drawable;
import sparkle.math.Vector2;
import sparkle.paints.Color;
import sparkle.paints.Paint;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public final class Polygon extends Drawable {
    private Vector2[] points;
    private Paint fill;
    private Paint stroke;
    private StrokeType strokeType;
    private Stroke strokeObject;
    private float strokeWidth;

    public Polygon() {
        this(null, null, null, null, 0);
    }

    public Polygon(Vector2[] points, Paint fill) {
        this(points, fill, null, null, 0);
    }

    public Polygon(Vector2[] points, Paint stroke, float strokeWidth) {
        this(points, null, stroke, null, strokeWidth);
    }

    public Polygon(Vector2[] points, Paint fill, Paint stroke, StrokeType strokeType, float strokeWidth) {
        this.points = (points == null ? new Vector2[]{} : copyPoints(points));
        this.fill = Objects.requireNonNullElse(fill, Color.TRANSPARENT);
        this.stroke = Objects.requireNonNullElse(stroke, Color.TRANSPARENT);
        this.strokeType = Objects.requireNonNullElse(strokeType, StrokeType.SQUARED);
        this.strokeWidth = strokeWidth;
        strokeObject = this.strokeType.build(this.strokeWidth);
    }

    @Override
    public boolean isOutsideScreen() {
        return super.isOutsideScreen(points, (stroke.equals(Color.TRANSPARENT) || strokeWidth == 0 ? null : strokeObject));
    }

    @Override
    protected void render(Graphics2D graphics) {
        var fillIsTransparent = fill.equals(Color.TRANSPARENT);
        var strokeIsTransparent = stroke.equals(Color.TRANSPARENT) || strokeWidth == 0;
        if (isOutsideScreen() || (fillIsTransparent && strokeIsTransparent)) {
            return;
        }
        var xPoints = super.getXPoints(points);
        var yPoints = super.getYPoints(points);
        if (!fillIsTransparent) {
            graphics.setPaint(fill.getPaint());
            graphics.fillPolygon(xPoints, yPoints, points.length);
        }
        if (!strokeIsTransparent) {
            graphics.setPaint(stroke.getPaint());
            graphics.setStroke(strokeObject);
            graphics.drawPolygon(xPoints, yPoints, points.length);
        }
    }

    public List<Vector2> getPoints() {
        return List.of(points);
    }

    public void setPoints(Vector2[] points) {
        this.points = (points == null ? new Vector2[]{} : copyPoints(points));
    }

    public Paint getFill() {
        return fill;
    }

    public void setFill(Paint fill) {
        this.fill = Objects.requireNonNullElse(fill, Color.TRANSPARENT);
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

    private Vector2[] copyPoints(Vector2[] points) {
        for (var point : points) {
            Objects.requireNonNull(point);
        }
        var result = new Vector2[points.length];
        System.arraycopy(points, 0, result, 0, points.length);
        return result;
    }
}