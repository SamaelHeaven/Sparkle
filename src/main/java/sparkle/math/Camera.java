package sparkle.math;

import sparkle.core.Component;
import sparkle.core.Game;
import sparkle.core.GameObject;

public final class Camera extends Vector2 {
    public void centerOn(Component component) {
        centerOn(component.getGameObject());
    }

    public void centerOn(GameObject gameObject) {
        centerOn(gameObject.getBounds());
    }

    public void centerOn(Bounds bounds) {
        super.set(bounds.getCenter().minus(Game.getSize().divide(2)));
    }

    public void shake(float force) {
        var moveX = (Math.random() * force * 2 - force);
        var moveY = (Math.random() * force * 2 - force);
        super.set(super.plus((float) moveX, (float) moveY));
    }
}