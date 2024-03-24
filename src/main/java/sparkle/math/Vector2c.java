package sparkle.math;

public final class Vector2c extends Vector2Base {
    private final Vector2 vector2;

    public Vector2c() {
        this(0);
    }

    public Vector2c(float xy) {
        this(xy, xy);
    }

    public Vector2c(float x, float y) {
        this(new Vector2(x, y));
    }

    public Vector2c(Vector2Base vector2Base) {
        if (vector2Base instanceof Vector2c vector2C) {
            this.vector2 = vector2C.vector2;
            return;
        }
        this.vector2 = (Vector2) vector2Base;
    }

    @Override
    public float getX() {
        return vector2.getX();
    }

    @Override
    public float getY() {
        return vector2.getY();
    }
}