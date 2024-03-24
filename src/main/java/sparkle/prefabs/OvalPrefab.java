package sparkle.prefabs;

import sparkle.core.Drawable;
import sparkle.core.GameObject;
import sparkle.drawables.Oval;

public final class OvalPrefab extends BasicDrawablePrefab {
    @Override
    protected String getTagName() {
        return "oval";
    }

    @Override
    protected Drawable getDrawable(GameObject gameObject) {
        var oval = new Oval(gameObject.getPosition(), gameObject.getSize(), super.getFill(), super.getStroke(), super.getStrokeType(), super.getStrokeWidth(), super.getRotation());
        if (super.asSprite()) {
            var texture = oval.toTexture();
            super.setTexture(texture);
            return super.getDrawable(gameObject);
        }
        return oval;
    }
}