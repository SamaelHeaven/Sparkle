package sparkle.math;

public abstract sealed class Vector2Base permits Vector2c, Vector2 {
    public abstract float getX();

    public abstract float getY();

    public Vector2 plus(float xy) {
        return plus(xy, xy);
    }

    public Vector2 plus(Vector2Base vector2Base) {
        return plus(vector2Base.getX(), vector2Base.getY());
    }

    public Vector2 plus(float x, float y) {
        return new Vector2(getX() + x, getY() + y);
    }

    public Vector2 minus(float xy) {
        return minus(xy, xy);
    }

    public Vector2 minus(Vector2Base vector2Base) {
        return minus(vector2Base.getX(), vector2Base.getY());
    }

    public Vector2 minus(float x, float y) {
        return new Vector2(getX() - x, getY() - y);
    }

    public Vector2 multiply(float xy) {
        return multiply(xy, xy);
    }

    public Vector2 multiply(Vector2Base vector2Base) {
        return multiply(vector2Base.getX(), vector2Base.getY());
    }

    public Vector2 multiply(float x, float y) {
        return new Vector2(getX() * x, getY() * y);
    }

    public Vector2 divide(float xy) {
        return divide(xy, xy);
    }

    public Vector2 divide(Vector2Base vector2Base) {
        return divide(vector2Base.getX(), vector2Base.getY());
    }

    public Vector2 divide(float x, float y) {
        return new Vector2(getX() / x, getY() / y);
    }

    public Vector2 translateX(float x) {
        return new Vector2(getX() + x, getY());
    }

    public Vector2 translateY(float y) {
        return new Vector2(getX(), getY() + y);
    }

    public Vector2 clampX(float minX, float maxX) {
        if (getX() < minX) {
            return new Vector2(minX, getY());
        }
        if (getX() > maxX) {
            return new Vector2(maxX, getY());
        }
        return new Vector2(this);
    }

    public Vector2 clampY(float minY, float maxY) {
        if (getY() < minY) {
            return new Vector2(getX(), minY);
        }
        if (getY() > maxY) {
            return new Vector2(getX(), maxY);
        }
        return new Vector2(this);
    }

    public Vector2 round() {
        return new Vector2(Math.round(getX()), Math.round(getY()));
    }

    public Vector2 abs() {
        return new Vector2(Math.abs(getX()), Math.abs(getY()));
    }

    public Vector2 normalize() {
        float length = (float) Math.sqrt(getX() * getX() + getY() * getY());
        if (length != 0) {
            return new Vector2(getX() / length, getY() / length);
        }
        return new Vector2(0, 0);
    }

    public int getModifierX() {
        if (getX() == 0) {
            return 0;
        }
        return getX() > 0 ? 1 : -1;
    }

    public int getModifierY() {
        if (getY() == 0) {
            return 0;
        }
        return getY() > 0 ? 1 : -1;
    }

    @Override
    public String toString() {
        return "{ \"x\": " + getX() + " \"y\": " + getY() + " }";
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Vector2Base vector2Base) {
            return vector2Base.getX() == getX() && vector2Base.getY() == getY();
        }
        return false;
    }
}