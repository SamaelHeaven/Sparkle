package sparkle.drawables;

import sparkle.assets.Texture;
import sparkle.core.Drawable;
import sparkle.core.Game;
import sparkle.core.Interpolation;
import sparkle.math.Bounds;
import sparkle.math.Vector2;
import sparkle.paints.Color;
import sparkle.physics.RigidBody;

import java.awt.Rectangle;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Arrays;
import java.util.Objects;

public final class Sprite extends Drawable {
    private final Vector2 position;
    private final Vector2 size;
    private Interpolation interpolation;
    private Texture texture;
    private BufferedImage currentImage;
    private Color color;
    private float alpha;
    private float blur;
    private float rotation;
    private boolean flippedHorizontally;
    private boolean flippedVertically;

    public Sprite() {
        this(null, null, null);
    }

    public Sprite(Vector2 position, Vector2 size, Texture texture) {
        this(position, size, texture, null, 1, 0, 0, false, false);
    }

    public Sprite(Vector2 position, Vector2 size, Texture texture, Color color, float alpha, float blur, float rotation, boolean flippedHorizontally, boolean flippedVertically) {
        this.position = Objects.requireNonNullElse(position, new Vector2());
        this.size = Objects.requireNonNullElse(size, texture == null ? new Vector2() : new Vector2(texture.getSize()));
        this.texture = Objects.requireNonNullElse(texture, new Texture(((BufferedImage) null)));
        this.color = Objects.requireNonNullElse(color, Color.TRANSPARENT);
        this.alpha = Math.max(0, Math.min(1, alpha));
        this.blur = Math.max(0, blur);
        this.rotation = rotation;
        this.flippedHorizontally = flippedHorizontally;
        this.flippedVertically = flippedVertically;
        updateCurrentImage();
    }

    @Override
    protected void update() {
        var body = super.getComponent(RigidBody.class);
        if (body != null) {
            setRotation(body.getRotation());
        }
        var renderer = Game.getRenderer();
        var oldInterpolation = renderer.getInterpolation();
        if (interpolation != null) {
            renderer.setInterpolation(interpolation);
        }
        super.update();
        renderer.setInterpolation(oldInterpolation);
    }

    @Override
    public boolean isOutsideScreen() {
        return super.isOutsideScreen(position, size, null, rotation);
    }

    @Override
    protected void render(Graphics2D graphics) {
        if (isOutsideScreen() || alpha == 0 || currentImage == null) {
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

    public Vector2 getSize() {
        return size;
    }

    public Interpolation getInterpolation() {
        return interpolation;
    }

    public void setInterpolation(Interpolation interpolation) {
        this.interpolation = interpolation;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        if (this.texture == texture) {
            return;
        }
        this.texture = Objects.requireNonNullElse(texture, new Texture(((BufferedImage) null)));
        updateCurrentImage();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (this.color.equals(color)) {
            return;
        }
        this.color = Objects.requireNonNullElse(color, Color.TRANSPARENT);
        updateCurrentImage();
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        if (this.alpha == alpha) {
            return;
        }
        this.alpha = Math.max(0, Math.min(1, alpha));
        updateCurrentImage();
    }

    public float getBlur() {
        return blur;
    }

    public void setBlur(float blur) {
        this.blur = Math.max(blur, 0);
        updateCurrentImage();
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public boolean isFlippedHorizontally() {
        return flippedHorizontally;
    }

    public void setFlippedHorizontally(boolean flippedHorizontally) {
        if (this.flippedHorizontally == flippedHorizontally) {
            return;
        }
        this.flippedHorizontally = flippedHorizontally;
        updateCurrentImage();
    }

    public boolean isFlippedVertically() {
        return flippedVertically;
    }

    public void setFlippedVertically(boolean flippedVertically) {
        if (this.flippedVertically == flippedVertically) {
            return;
        }
        this.flippedVertically = flippedVertically;
        updateCurrentImage();
    }

    public Texture toTexture() {
        var bounds = getBounds();
        var texture = new Texture(Math.round(bounds.getWidth()), Math.round(bounds.getHeight()));
        var x = (bounds.getWidth() - size.getX()) / 2;
        var y = (bounds.getHeight() - size.getY()) / 2;
        render(texture, x, y);
        return texture;
    }

    public Bounds getBounds() {
        var position = getAnchor().plus(this.position);
        var bounds = new Rectangle(Math.round(position.getX()), Math.round(position.getY()), Math.round(size.getX()), Math.round(size.getY()));
        return new Bounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void render(Texture texture, float x, float y) {
        if (alpha == 0 || currentImage == null) {
            return;
        }
        var graphics = Objects.requireNonNull(texture).getImage().createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, Objects.requireNonNullElse(interpolation, Game.getRenderer().getInterpolation()).value);
        render(graphics, x, y);
        graphics.dispose();
    }

    private void render(Graphics2D graphics, float x, float y) {
        graphics.drawImage(currentImage, Math.round(x), Math.round(y), Math.round(size.getX()), Math.round(size.getY()), null);
    }

    private void updateCurrentImage() {
        if (texture.getImage() == null) {
            currentImage = null;
            return;
        }
        var image = handleHorizontalFlip(texture.getImage());
        image = handleVerticalFlip(image);
        image = handleColor(image);
        image = handleAlpha(image);
        currentImage = handleBlur(image);
    }

    private BufferedImage handleHorizontalFlip(BufferedImage image) {
        if (!flippedHorizontally) {
            return image;
        }
        var affineTransform = AffineTransform.getScaleInstance(-1, 1);
        affineTransform.translate(-image.getWidth(), 0);
        var op = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }

    private BufferedImage handleVerticalFlip(BufferedImage image) {
        if (!flippedVertically) {
            return image;
        }
        var affineTransform = AffineTransform.getScaleInstance(1, -1);
        affineTransform.translate(0, -image.getHeight());
        var op = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }

    private BufferedImage handleColor(BufferedImage image) {
        if (color.equals(Color.TRANSPARENT)) {
            return image;
        }
        var bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        var graphics = bufferedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.drawImage(image, 0, 0, null);
        graphics.setPaint(color.getPaint());
        graphics.setComposite(AlphaComposite.SrcAtop);
        graphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        graphics.dispose();
        return bufferedImage;
    }

    private BufferedImage handleAlpha(BufferedImage image) {
        if (alpha == 1) {
            return image;
        }
        var bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        var graphics = bufferedImage.createGraphics();
        var alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setComposite(alphaComposite);
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return bufferedImage;
    }

    private BufferedImage handleBlur(BufferedImage image) {
        if (blur == 0) {
            return image;
        }
        var kernelSize = Math.round(blur * 2 + 1);
        var matrix = new float[kernelSize * kernelSize];
        Arrays.fill(matrix, 1.0f / (kernelSize * kernelSize));
        var kernel = new Kernel(kernelSize, kernelSize, matrix);
        var convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);
        var bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        convolveOp.filter(image, bufferedImage);
        return bufferedImage;
    }
}