package sparkle.math;

public sealed class Vector2 extends Vector2Base permits ObservableVector2, Camera {
    private float x;
    private float y;

    public Vector2() {
        this(0, 0);
    }

    public Vector2(float xy) {
        this(xy, xy);
    }

    public Vector2(Vector2Base vector2Base) {
        this(vector2Base.getX(), vector2Base.getY());
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public float getX() {
        return x;
    }

    public void setX(float x) {
        set(x, y);
    }

    @Override
    public float getY() {
        return y;
    }

    public void setY(float y) {
        set(x, y);
    }

    public void set(float xy) {
        set(xy, xy);
    }

    public void set(Vector2Base vector2Base) {
        set(vector2Base.getX(), vector2Base.getY());
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }
}