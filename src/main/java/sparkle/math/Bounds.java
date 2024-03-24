package sparkle.math;

public final class Bounds {
    private final float x;
    private final float y;
    private final float width;
    private final float height;

    public Bounds(Vector2Base position, Vector2Base size) {
        this(position.getX(), position.getY(), size.getX(), size.getY());
    }

    public Bounds(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean intersectsWith(Bounds bounds) {
        return x < bounds.x + bounds.width && x + width > bounds.x && y < bounds.y + bounds.height && y + height > bounds.y;
    }

    public boolean contains(Vector2Base position) {
        return intersectsWith(new Bounds(position.getX(), position.getY(), 0, 0));
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Vector2c getPosition() {
        return new Vector2c(x, y);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Vector2c getSize() {
        return new Vector2c(width, height);
    }

    public Vector2c getCenter() {
        var centerX = x + width / 2.0f;
        var centerY = y + height / 2.0f;
        return new Vector2c(centerX, centerY);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Bounds bounds) {
            return x == bounds.x && y == bounds.y && width == bounds.width && height == bounds.height;
        }
        return super.equals(object);
    }

    @Override
    public String toString() {
        return "{ \"x\": " + x + ", \"y\": " + y + ", \"width\": " + width + ", \"height\": " + height + " }";
    }
}