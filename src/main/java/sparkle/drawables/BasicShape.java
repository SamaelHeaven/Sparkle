package sparkle.drawables;

import sparkle.assets.Texture;
import sparkle.core.Drawable;
import sparkle.math.Bounds;
import sparkle.math.Vector2;
import sparkle.paints.Color;
import sparkle.paints.Paint;
import sparkle.physics.RigidBody;

import java.awt.*;
import java.util.Objects;

public abstract sealed class BasicShape extends Drawable permits Oval, Rectangle, RoundedRectangle {
    private final Vector2 position;
    private final Vector2 size;
    protected Stroke strokeObject;
    private StrokeType strokeType;
    private Paint fill;
    private Paint stroke;
    private float rotation;
    private float strokeWidth;

    public BasicShape() {
        this(null, null, null);
    }

    public BasicShape(Vector2 position, Vector2 size, Paint fill) {
        this(position, size, fill, null, null, 0, 0);
    }

    public BasicShape(Vector2 position, Vector2 size, Paint stroke, float strokeWidth) {
        this(position, size, null, stroke, null, strokeWidth, 0);
    }

    public BasicShape(Vector2 position, Vector2 size, Paint fill, Paint stroke, StrokeType strokeType, float strokeWidth, float rotation) {
        this.position = Objects.requireNonNullElse(position, new Vector2());
        this.size = Objects.requireNonNullElse(size, new Vector2());
        this.fill = Objects.requireNonNullElse(fill, Color.TRANSPARENT);
        this.stroke = Objects.requireNonNullElse(stroke, Color.TRANSPARENT);
        this.strokeType = Objects.requireNonNullElse(strokeType, getDefaultStrokeType());
        this.strokeWidth = Math.max(0, strokeWidth);
        this.rotation = rotation;
        strokeObject = this.strokeType.build(this.strokeWidth);
    }

    @Override
    protected void update() {
        var body = super.getComponent(RigidBody.class);
        if (body != null) {
            setRotation(body.getRotation());
        }
        super.update();
    }

    @Override
    public boolean isOutsideScreen() {
        return super.isOutsideScreen(position, size, (stroke.equals(Color.TRANSPARENT) || strokeWidth == 0 ? null : strokeObject), rotation);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
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
        this.strokeType = Objects.requireNonNullElse(strokeType, getDefaultStrokeType());
        strokeObject = this.strokeType.build(strokeWidth);
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = Math.max(0, strokeWidth);
        strokeObject = this.strokeType.build(this.strokeWidth);
    }

    public Texture toTexture() {
        var bounds = getVisualBounds();
        var texture = new Texture(Math.round(bounds.getWidth()), Math.round(bounds.getHeight()));
        var x = (bounds.getWidth() - size.getX()) / 2;
        var y = (bounds.getHeight() - size.getY()) / 2;
        render(texture, x, y);
        return texture;
    }

    public void render(Texture texture, float x, float y) {
        var fillIsTransparent = fill.equals(Color.TRANSPARENT);
        var strokeIsTransparent = stroke.equals(Color.TRANSPARENT) || strokeWidth == 0;
        if (fillIsTransparent && strokeIsTransparent) {
            return;
        }
        var graphics = Objects.requireNonNull(texture).getImage().createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        render(graphics, x, y);
        graphics.dispose();
    }

    public Bounds getBounds() {
        var position = getAnchor().plus(this.position);
        return new Bounds(position, size);
    }

    public Bounds getVisualBounds() {
        var position = getAnchor().plus(this.position);
        var bounds = strokeObject.createStrokedShape(new java.awt.Rectangle(Math.round(position.getX()), Math.round(position.getY()), Math.round(size.getX()), Math.round(size.getY()))).getBounds();
        return new Bounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    protected abstract void render(Graphics2D graphics, float x, float y);

    protected abstract StrokeType getDefaultStrokeType();
}