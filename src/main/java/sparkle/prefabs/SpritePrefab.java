package sparkle.prefabs;

import sparkle.assets.Texture;
import sparkle.core.Drawable;
import sparkle.core.GameObject;
import sparkle.core.Interpolation;
import sparkle.drawables.Sprite;
import sparkle.paints.Color;
import sparkle.xml.XMLElement;

public class SpritePrefab extends DrawablePrefab {
    private Interpolation interpolation;
    private Texture texture;
    private Color color;
    private float alpha;
    private float blur;
    private float rotation;
    private boolean flippedHorizontally;
    private boolean flippedVertically;

    public SpritePrefab() {
        alpha = 1;
        interpolation = Interpolation.BILINEAR;
    }

    @Override
    protected String getTagName() {
        return "sprite";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {
        super.setProperties(xmlElement);
        interpolation = xmlElement.getEnum(Interpolation.class, "interpolation", Interpolation.BILINEAR);
        color = xmlElement.getColor("color");
        alpha = xmlElement.getNumber("alpha", 1);
        blur = xmlElement.getNumber("blur");
        rotation = xmlElement.getNumber("rotation");
        flippedHorizontally = xmlElement.getBoolean("flippedHorizontally");
        flippedVertically = xmlElement.getBoolean("flippedVertically");
        if (super.getClass() == SpritePrefab.class) {
            var cacheTexture = xmlElement.getBoolean("cacheTexture", true);
            texture = xmlElement.getTexture("texture", null, cacheTexture);
        }
    }

    @Override
    protected Drawable getDrawable(GameObject gameObject) {
        var sprite = new Sprite(gameObject.getPosition(), gameObject.getSize(), texture, color, alpha, blur, rotation, flippedHorizontally, flippedVertically);
        if (gameObject.getSize().getX() == 0 && texture != null) {
            gameObject.getSize().setX(texture.getWidth());
        }
        if (gameObject.getSize().getY() == 0 && texture != null) {
            gameObject.getSize().setY(texture.getHeight());
        }
        sprite.setInterpolation(interpolation);
        return sprite;
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
        this.texture = texture;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getBlur() {
        return blur;
    }

    public void setBlur(float blur) {
        this.blur = blur;
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
        this.flippedHorizontally = flippedHorizontally;
    }

    public boolean isFlippedVertically() {
        return flippedVertically;
    }

    public void setFlippedVertically(boolean flippedVertically) {
        this.flippedVertically = flippedVertically;
    }
}