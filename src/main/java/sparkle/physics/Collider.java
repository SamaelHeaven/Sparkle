package sparkle.physics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import sparkle.math.Vector2Base;

public enum Collider {
    BOX {
        @Override
        Shape buildShape(Vector2Base size) {
            var shape = new PolygonShape();
            shape.setAsBox((size.getX() * Physics.PIXELS_TO_METERS) / 2, (size.getY() * Physics.PIXELS_TO_METERS) / 2);
            return shape;
        }
    }, CIRCLE {
        @Override
        Shape buildShape(Vector2Base size) {
            if (size.getX() != size.getY()) {
                throw new IllegalArgumentException("Size is not a valid circle (" + size + ")");
            }
            var circle = new CircleShape();
            circle.setRadius((size.getX() / 2) * Physics.PIXELS_TO_METERS);
            return circle;
        }
    };

    abstract Shape buildShape(Vector2Base size);
}