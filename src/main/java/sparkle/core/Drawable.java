package sparkle.core;

import sparkle.math.Vector2;
import sparkle.math.Vector2Base;

import java.awt.*;
import java.util.Objects;

public abstract class Drawable extends Component {
    private RenderingMode renderingMode = RenderingMode.WORLD;
    private Vector2Base anchor = new Vector2();

    public abstract boolean isOutsideScreen();

    protected abstract void render(Graphics2D graphics);

    @Override
    protected void update() {
        Game.getRenderer().draw(this);
    }

    public RenderingMode getRenderingMode() {
        return renderingMode;
    }

    public void setRenderingMode(RenderingMode renderingMode) {
        this.renderingMode = Objects.requireNonNullElse(renderingMode, RenderingMode.WORLD);
    }

    public Vector2Base getAnchor() {
        return anchor;
    }

    public void setAnchor(Vector2Base anchor) {
        this.anchor = Objects.requireNonNullElse(anchor, new Vector2());
    }

    protected boolean isOutsideScreen(Vector2Base position, Vector2Base size, Stroke strokeObject, float rotation) {
        if (rotation == 0) {
            return isOutsideScreen(position, size, strokeObject);
        }
        var points = getRotatedPoints(position, size, (float) Math.toRadians(rotation));
        return isOutsideScreen(points, strokeObject);
    }

    protected boolean isOutsideScreen(Vector2Base[] polygon, Stroke strokeObject) {
        var awtPolygon = new Polygon(getXPoints(polygon), getYPoints(polygon), polygon.length);
        var shape = ((strokeObject == null ? awtPolygon : strokeObject.createStrokedShape(awtPolygon)));
        return !shape.intersects(0, 0, Game.getWidth(), Game.getHeight());
    }

    protected int[] getXPoints(Vector2Base[] points) {
        var result = new int[points.length];
        for (var i = 0; i < result.length; i++) {
            result[i] = Math.round(renderingMode.getModifierX(anchor.getX() + points[i].getX()));
        }
        return result;
    }

    protected int[] getYPoints(Vector2Base[] points) {
        var result = new int[points.length];
        for (var i = 0; i < result.length; i++) {
            result[i] = Math.round(renderingMode.getModifierY(anchor.getY() + points[i].getY()));
        }
        return result;
    }

    private boolean isOutsideScreen(Vector2Base position, Vector2Base size, Stroke strokeObject) {
        var x = renderingMode.getModifierX(anchor.getX() + position.getX());
        var y = renderingMode.getModifierY(anchor.getY() + position.getY());
        var rectangle = new Rectangle(Math.round(x), Math.round(y), Math.round(size.getX()), Math.round(size.getY()));
        var shape = ((strokeObject == null ? rectangle : strokeObject.createStrokedShape(rectangle)));
        return !shape.intersects(0, 0, Game.getWidth(), Game.getHeight());
    }

    private Vector2Base[] getRotatedPoints(Vector2Base position, Vector2Base size, float rotation) {
        var x = position.getX();
        var y = position.getY();
        var width = size.getX();
        var height = size.getY();
        var centerX = x + width / 2;
        var centerY = y + height / 2;
        var topLeft = rotatePoint(x, y, centerX, centerY, rotation);
        var topRight = rotatePoint(x + width, y, centerX, centerY, rotation);
        var bottomLeft = rotatePoint(x, y + height, centerX, centerY, rotation);
        var bottomRight = rotatePoint(x + width, y + height, centerX, centerY, rotation);
        return new Vector2Base[]{topLeft, bottomLeft, bottomRight, topRight};
    }

    private Vector2Base rotatePoint(float x, float y, float centerX, float centerY, float rotation) {
        var translatedX = x - centerX;
        var translatedY = y - centerY;
        var rotatedX = translatedX * Math.cos(rotation) - translatedY * Math.sin(rotation);
        var rotatedY = translatedX * Math.sin(rotation) + translatedY * Math.cos(rotation);
        var finalX = rotatedX + centerX;
        var finalY = rotatedY + centerY;
        return new Vector2((float) finalX, (float) finalY);
    }
}