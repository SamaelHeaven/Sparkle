package sparkle.prefabs;

import sparkle.core.Drawable;
import sparkle.core.GameObject;
import sparkle.drawables.RoundedRectangle;
import sparkle.xml.XMLElement;

public final class RectanglePrefab extends BasicDrawablePrefab {
    private int radius;

    @Override
    protected String getTagName() {
        return "rectangle";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {
        super.setProperties(xmlElement);
        radius = (int) xmlElement.getNumber("radius");
    }

    @Override
    protected Drawable getDrawable(GameObject gameObject) {
        var rectangle = new RoundedRectangle(gameObject.getPosition(), gameObject.getSize(), super.getFill(), super.getStroke(), super.getStrokeType(), super.getStrokeWidth(), super.getRotation(), radius);
        if (super.asSprite()) {
            var texture = rectangle.toTexture();
            super.setTexture(texture);
            return super.getDrawable(gameObject);
        }
        return rectangle;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}