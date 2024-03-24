package sparkle.drawables;

import sparkle.assets.Font;
import sparkle.assets.Texture;
import sparkle.core.Drawable;
import sparkle.math.Bounds;
import sparkle.math.Vector2;
import sparkle.math.Vector2c;
import sparkle.paints.Color;
import sparkle.paints.Paint;
import sparkle.physics.RigidBody;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public final class Text extends Drawable {
    public static final int DEFAULT_FONT_SIZE = 12;
    private final Vector2 position;
    private final Vector2 size;
    private FontMetrics fontMetrics;
    private Font font;
    private Paint fill;
    private Paint stroke;
    private Stroke strokeObject;
    private StrokeType strokeType;
    private String text;
    private float fontSize;
    private float strokeWidth;
    private float rotation;

    public Text() {
        this(null);
    }

    public Text(Vector2 position) {
        this(position, null, null, null);
    }

    public Text(Vector2 position, Font font, Paint fill, String text) {
        this(position, font, fill, null, null, text, DEFAULT_FONT_SIZE, 0, 0);
    }

    public Text(Vector2 position, Font font, Paint fill, Paint stroke, StrokeType strokeType, String text, float fontSize, float strokeWidth, float rotation) {
        this.position = Objects.requireNonNullElse(position, new Vector2());
        size = new Vector2();
        this.font = Objects.requireNonNullElse(font, new Font(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, (int) fontSize)));
        this.fill = Objects.requireNonNullElse(fill, Color.TRANSPARENT);
        this.stroke = Objects.requireNonNullElse(stroke, Color.TRANSPARENT);
        this.strokeType = Objects.requireNonNullElse(strokeType, StrokeType.ROUNDED);
        this.text = Objects.requireNonNullElse(text, "");
        this.fontSize = Math.max(0, fontSize);
        this.strokeWidth = Math.max(0, strokeWidth);
        this.rotation = rotation;
        strokeObject = this.strokeType.build(this.strokeWidth);
        updateFont();
        updateSize();
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

    @Override
    protected void render(Graphics2D graphics) {
        var fillIsTransparent = fill.equals(Color.TRANSPARENT);
        var strokeIsTransparent = stroke.equals(Color.TRANSPARENT) || strokeWidth == 0;
        if (isOutsideScreen() || (fillIsTransparent && strokeIsTransparent)) {
            return;
        }
        var x = super.getRenderingMode().getModifierX(super.getAnchor().getX() + position.getX());
        var y = super.getRenderingMode().getModifierY(super.getAnchor().getY() + position.getY());
        var oldTransform = graphics.getTransform();
        if (rotation != 0) {
            graphics.rotate(Math.toRadians(rotation), x + (size.getX() / 2.0), y + size.getY() / 2.0);
        }
        render(graphics, x, y);
        graphics.setTransform(oldTransform);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2c getSize() {
        return new Vector2c(size);
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        if (this.font.equals(font)) {
            return;
        }
        this.font = Objects.requireNonNullElse(font, new Font(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, (int) fontSize)));
        updateFont();
        updateSize();
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (this.text.equals(text)) {
            return;
        }
        this.text = Objects.requireNonNullElse(text, "");
        updateSize();
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        if (this.fontSize == fontSize) {
            return;
        }
        this.fontSize = Math.max(0, fontSize);
        updateFont();
        updateSize();
    }

    public StrokeType getStrokeType() {
        return strokeType;
    }

    public void setStrokeType(StrokeType strokeType) {
        this.strokeType = Objects.requireNonNullElse(strokeType, StrokeType.ROUNDED);
        strokeObject = this.strokeType.build(strokeWidth);
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = Math.max(0, strokeWidth);
        strokeObject = strokeType.build(this.strokeWidth);
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Texture toTexture() {
        var bounds = getVisualBounds();
        var texture = new Texture(Math.round(bounds.getWidth()), Math.round(bounds.getHeight()));
        var x = (bounds.getWidth() - size.getX()) / 2;
        var y = (bounds.getHeight() - size.getY()) / 2;
        render(texture, x, y);
        return texture;
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

    private void render(Graphics2D graphics, float x, float y) {
        var fillIsTransparent = fill.equals(Color.TRANSPARENT);
        var strokeIsTransparent = stroke.equals(Color.TRANSPARENT) || strokeWidth == 0;
        graphics.setFont(font.getFont());
        var fontRenderContext = graphics.getFontRenderContext();
        var currentY = y + fontMetrics.getHeight() - fontMetrics.getMaxDescent();
        for (var line : text.split("\n")) {
            var glyphVector = font.getFont().createGlyphVector(fontRenderContext, line);
            var textShape = glyphVector.getOutline();
            graphics.translate(x, currentY);
            if (!fillIsTransparent) {
                graphics.setPaint(fill.getPaint());
                graphics.fill(textShape);
            }
            if (!strokeIsTransparent) {
                graphics.setPaint(stroke.getPaint());
                graphics.setStroke(strokeObject);
                graphics.draw(textShape);
            }
            graphics.translate(-x, -currentY);
            currentY += fontMetrics.getMaxAscent();
        }
    }

    private void updateSize() {
        var bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        var graphics = bufferedImage.getGraphics();
        fontMetrics = graphics.getFontMetrics(font.getFont());
        var lines = text.split("\n");
        var width = 0.0f;
        for (var line : lines) {
            width = (float) Math.max(width, fontMetrics.getStringBounds(line, graphics).getWidth());
        }
        size.set(width, (fontMetrics.getMaxAscent() * (lines.length)) + fontMetrics.getMaxDescent());
        bufferedImage.flush();
        graphics.dispose();
    }

    private void updateFont() {
        font = new Font(font.getFont().deriveFont(fontSize));
    }
}