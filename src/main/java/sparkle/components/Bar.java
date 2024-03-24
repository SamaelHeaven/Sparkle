package sparkle.components;

import sparkle.assets.Font;
import sparkle.assets.Texture;
import sparkle.core.Component;
import sparkle.core.Game;
import sparkle.core.Interpolation;
import sparkle.core.RenderingMode;
import sparkle.drawables.RoundedRectangle;
import sparkle.drawables.Sprite;
import sparkle.drawables.StrokeType;
import sparkle.drawables.Text;
import sparkle.math.Alignment;
import sparkle.math.Bounds;
import sparkle.math.Vector2;
import sparkle.paints.Color;
import sparkle.paints.Paint;
import sparkle.physics.RigidBody;

import java.util.Objects;

public final class Bar extends Component {
    private final RoundedRectangle background;
    private final RoundedRectangle foreground;
    private final RoundedRectangle borders;
    private final Text text;
    private final Sprite sprite;
    private Alignment textAlignment;
    private FillDirection fillDirection;
    private float value;
    private boolean spriteCleaned;

    public Bar() {
        this(null, null);
    }

    public Bar(Vector2 position, Vector2 size) {
        background = new RoundedRectangle(position, size, Color.GRAY, 10);
        foreground = new RoundedRectangle(null, null, Color.WHITE, background.getRadius());
        borders = new RoundedRectangle(null, null, Color.BLACK, 1, background.getRadius());
        text = new Text(null, null, Color.BLACK, null);
        sprite = new Sprite(background.getPosition(), null, null);
        textAlignment = Alignment.CENTER;
        fillDirection = FillDirection.RIGHT;
        sprite.setRenderingMode(RenderingMode.WORLD);
        sprite.setInterpolation(Interpolation.BILINEAR);
        value = 1;
        updateDrawables();
    }

    @Override
    protected void update() {
        updateSprite();
        var body = super.getComponent(RigidBody.class);
        if (body != null) {
            setRotation(body.getRotation());
        }
        Game.getRenderer().draw(sprite);
    }

    @Override
    protected void onPositionChanged(float oldX, float oldY, float newX, float newY) {
        updateDrawables();
    }

    @Override
    protected void onSizeChanged(float oldX, float oldY, float newX, float newY) {
        updateDrawables();
    }

    public Sprite toSprite(Vector2 position, Vector2 size) {
        updateSprite();
        var sprite = new Sprite(position, size, this.sprite.toTexture(), this.sprite.getColor(), this.sprite.getAlpha(), this.sprite.getBlur(), this.sprite.getRotation(), this.sprite.isFlippedHorizontally(), this.sprite.isFlippedVertically());
        sprite.setAnchor(new Vector2(this.sprite.getAnchor()));
        return sprite;
    }

    public Texture toTexture() {
        updateSprite();
        return sprite.toTexture();
    }

    public Vector2 getPosition() {
        return background.getPosition();
    }

    public Vector2 getSize() {
        return background.getSize();
    }

    public Bounds getBounds() {
        return new Bounds(background.getPosition(), background.getSize());
    }

    public Bounds getVisualBounds() {
        updateSprite();
        return sprite.getBounds();
    }

    public Alignment getTextAlignment() {
        return textAlignment;
    }

    public void setTextAlignment(Alignment textAlignment) {
        this.textAlignment = Objects.requireNonNullElse(textAlignment, Alignment.CENTER);
        spriteCleaned = false;
    }

    public Interpolation getInterpolation() {
        return sprite.getInterpolation();
    }

    public void setInterpolation(Interpolation interpolation) {
        sprite.setInterpolation(interpolation);
    }

    public Color getColor() {
        return sprite.getColor();
    }

    public void setColor(Color color) {
        sprite.setColor(color);
    }

    public float getAlpha() {
        return sprite.getAlpha();
    }

    public void setAlpha(float alpha) {
        sprite.setAlpha(alpha);
    }

    public float getBlur() {
        return sprite.getBlur();
    }

    public void setBlur(float blur) {
        sprite.setBlur(blur);
    }

    public float getRotation() {
        return sprite.getRotation();
    }

    public void setRotation(float rotation) {
        sprite.setRotation(rotation);
    }

    public boolean isFlippedHorizontally() {
        return sprite.isFlippedHorizontally();
    }

    public void setFlippedHorizontally(boolean flippedHorizontally) {
        sprite.setFlippedHorizontally(flippedHorizontally);
    }

    public boolean isFlippedVertically() {
        return sprite.isFlippedVertically();
    }

    public void setFlippedVertically(boolean flippedVertically) {
        sprite.setFlippedVertically(flippedVertically);
    }

    public FillDirection getFillDirection() {
        return fillDirection;
    }

    public void setFillDirection(FillDirection fillDirection) {
        if (this.fillDirection == fillDirection) {
            return;
        }
        this.fillDirection = Objects.requireNonNullElse(fillDirection, FillDirection.RIGHT);
        spriteCleaned = false;
    }

    public RenderingMode getRenderingMode() {
        return sprite.getRenderingMode();
    }

    public void setRenderingMode(RenderingMode renderingMode) {
        sprite.setRenderingMode(renderingMode);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        var newValue = Math.max(0, Math.min(1, value));
        if (this.value == newValue) {
            return;
        }
        this.value = newValue;
        updateDrawables();
    }

    public Paint getBackground() {
        return background.getFill();
    }

    public void setBackground(Paint background) {
        if (this.background.getFill().equals(background)) {
            return;
        }
        this.background.setFill(background);
        spriteCleaned = false;
    }

    public Paint getForeground() {
        return foreground.getFill();
    }

    public void setForeground(Paint foreground) {
        if (this.foreground.getFill().equals(foreground)) {
            return;
        }
        this.foreground.setFill(foreground);
        spriteCleaned = false;
    }

    public Paint getStroke() {
        return borders.getStroke();
    }

    public void setStroke(Paint stroke) {
        if (borders.getStroke().equals(stroke)) {
            return;
        }
        borders.setStroke(stroke);
        spriteCleaned = false;
    }

    public StrokeType getStrokeType() {
        return borders.getStrokeType();
    }

    public void setStrokeType(StrokeType strokeType) {
        if (background.getStrokeType() == strokeType) {
            return;
        }
        background.setStrokeType(strokeType);
        foreground.setStrokeType(strokeType);
        borders.setStrokeType(strokeType);
        spriteCleaned = false;
    }

    public float getStrokeWidth() {
        return borders.getStrokeWidth();
    }

    public void setStrokeWidth(float strokeWidth) {
        if (borders.getStrokeWidth() == strokeWidth) {
            return;
        }
        borders.setStrokeWidth(strokeWidth);
        spriteCleaned = false;
    }

    public int getRadius() {
        return borders.getRadius();
    }

    public void setRadius(int radius) {
        if (background.getRadius() == radius) {
            return;
        }
        background.setRadius(radius);
        foreground.setRadius(radius);
        borders.setRadius(radius);
        spriteCleaned = false;
    }

    public String getText() {
        return text.getText();
    }

    public void setText(String text) {
        if (this.text.getText().equals(text)) {
            return;
        }
        this.text.setText(text);
        updateDrawables();
    }

    public Font getFont() {
        return text.getFont();
    }

    public void setFont(Font font) {
        if (text.getFont() == font) {
            return;
        }
        text.setFont(font);
        updateDrawables();
    }

    public float getFontSize() {
        return text.getFontSize();
    }

    public void setFontSize(float fontSize) {
        if (text.getFontSize() == fontSize) {
            return;
        }
        text.setFontSize(fontSize);
        updateDrawables();
    }

    public Paint getTextFill() {
        return text.getFill();
    }

    public void setTextFill(Paint fill) {
        if (text.getFill().equals(fill)) {
            return;
        }
        text.setFill(fill);
        spriteCleaned = false;
    }

    public Paint getTextStroke() {
        return text.getStroke();
    }

    public void setTextStroke(Paint stroke) {
        if (text.getStroke().equals(stroke)) {
            return;
        }
        text.setStroke(stroke);
        spriteCleaned = false;
    }

    public StrokeType getTextStrokeType() {
        return text.getStrokeType();
    }

    public void setTextStrokeType(StrokeType strokeType) {
        if (text.getStrokeType() == strokeType) {
            return;
        }
        text.setStrokeType(strokeType);
        spriteCleaned = false;
    }

    public float getTextStrokeWidth() {
        return text.getStrokeWidth();
    }

    public void setTextStrokeWidth(float strokeWidth) {
        if (text.getStrokeWidth() == strokeWidth) {
            return;
        }
        text.setStrokeWidth(strokeWidth);
        spriteCleaned = false;
    }

    private void updateDrawables() {
        if (!text.getText().isEmpty()) {
            background.getSize().setX(Math.max(borders.getSize().getX(), text.getSize().getX() + 12));
            background.getSize().setY(Math.max(borders.getSize().getY(), text.getSize().getY() + 4));
        }
        foreground.getPosition().set(background.getPosition());
        foreground.getSize().set(background.getSize());
        borders.getSize().set(background.getSize());
        fillDirection.update(this);
        spriteCleaned = false;
    }

    private void updateSprite() {
        if (spriteCleaned) {
            return;
        }
        var bounds = borders.getVisualBounds();
        var texture = sprite.getTexture();
        if (sprite.getTexture().getWidth() != Math.round(bounds.getWidth()) || sprite.getTexture().getHeight() != Math.round(bounds.getHeight())) {
            if (texture.getImage() != null) {
                texture.getImage().flush();
            }
            texture = new Texture(Math.round(bounds.getWidth()), Math.round(bounds.getHeight()));
        } else {
            var graphics = texture.getImage().createGraphics();
            graphics.setBackground(new java.awt.Color(255, 255, 255, 0));
            graphics.clearRect(0, 0, texture.getWidth(), texture.getHeight());
            graphics.dispose();
        }
        var backgroundPosition = texture.getSize().minus(background.getSize()).divide(2);
        var textPosition = textAlignment.getPositionOnBounds(text.getSize(), new Bounds(backgroundPosition, background.getSize()));
        background.render(texture, backgroundPosition.getX(), backgroundPosition.getY());
        foreground.render(texture, backgroundPosition.getX(), backgroundPosition.getY());
        borders.render(texture, backgroundPosition.getX(), backgroundPosition.getY());
        text.render(texture, textPosition.getX(), textPosition.getY());
        sprite.setTexture(texture);
        sprite.getSize().set(sprite.getTexture().getSize());
        sprite.setAnchor(background.getSize().minus(sprite.getSize()).divide(2));
        spriteCleaned = true;
    }

    public enum FillDirection {
        RIGHT {
            @Override
            protected void update(Bar bar) {
                bar.foreground.getSize().setX((bar.background.getSize().getX() * bar.value));
            }
        }, LEFT {
            @Override
            protected void update(Bar bar) {
                bar.foreground.getSize().setX((bar.background.getSize().getX() * bar.value));
                bar.foreground.getPosition().setX(bar.background.getPosition().getX() + bar.background.getSize().getX() - bar.foreground.getSize().getX());
            }
        }, BOTTOM {
            @Override
            protected void update(Bar bar) {
                bar.foreground.getSize().setY((bar.background.getSize().getY() * bar.value));
            }
        }, TOP {
            @Override
            protected void update(Bar bar) {
                bar.foreground.getSize().setY((bar.background.getSize().getY() * bar.value));
                bar.foreground.getPosition().setY(bar.background.getPosition().getY() + bar.background.getSize().getY() - bar.foreground.getSize().getY());
            }
        };

        protected abstract void update(Bar bar);
    }
}