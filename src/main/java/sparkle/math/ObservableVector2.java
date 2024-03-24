package sparkle.math;

import java.util.Objects;

public final class ObservableVector2 extends Vector2 {
    private final OnChangedCallback onChangedCallback;

    public ObservableVector2(OnChangedCallback onChangedCallback) {
        this(0, 0, onChangedCallback);
    }

    public ObservableVector2(float xy, OnChangedCallback onChangedCallback) {
        this(xy, xy, onChangedCallback);
    }

    public ObservableVector2(Vector2Base vector2Base, OnChangedCallback onChangedCallback) {
        this((vector2Base == null ? 0 : vector2Base.getX()), (vector2Base == null ? 0 : vector2Base.getY()), onChangedCallback);
    }

    public ObservableVector2(float x, float y, OnChangedCallback onChangedCallback) {
        set(x, y);
        this.onChangedCallback = Objects.requireNonNullElse(onChangedCallback, (ox, oy, nx, ny) -> {});
    }

    @Override
    public void set(float x, float y) {
        var oldX = super.getX();
        var oldY = super.getY();
        super.set(x, y);
        if ((oldX != x || oldY != y) && onChangedCallback != null) {
            onChangedCallback.onChanged(oldX, oldY, x, y);
        }
    }

    public interface OnChangedCallback {
        void onChanged(float oldX, float oldY, float newX, float newY);
    }
}