package sparkle.physics;

import sparkle.core.GameObject;
import sparkle.math.Vector2c;

public interface RayCastCallback {
    float reportRayCast(GameObject gameObject, Vector2c point, Vector2c normal, float fraction);
}