package sparkle.components;

import sparkle.core.Component;
import sparkle.core.Game;
import sparkle.core.GameObject;
import sparkle.math.*;

import java.util.Objects;

public final class Aligner extends Component {
    private final ObservableVector2 offset;
    private final Vector2 lastPosition = new Vector2();
    private final Vector2 lastSize = new Vector2();
    private Vector2Base position;
    private Vector2Base size;
    private Alignment alignment;

    public Aligner(Alignment alignment) {
        this(alignment, (Bounds) null);
    }

    public Aligner(Alignment alignment, Bounds bounds) {
        this(alignment, bounds, null);
    }

    public Aligner(Alignment alignment, GameObject gameObject) {
        this(alignment, gameObject, null);
    }

    public Aligner(Alignment alignment, Bounds bounds, Vector2Base offset) {
        this.alignment = Objects.requireNonNullElse(alignment, Alignment.TOP_LEFT);
        this.offset = new ObservableVector2(offset, (oX, oY, nX, nY) -> trigger());
        position = bounds == null ? new Vector2() : bounds.getPosition();
        size = bounds == null ? Game.getSize() : bounds.getSize();
    }

    public Aligner(Alignment alignment, GameObject gameObject, Vector2Base offset) {
        this.alignment = Objects.requireNonNullElse(alignment, Alignment.TOP_LEFT);
        this.offset = new ObservableVector2(offset, (oX, oY, nX, nY) -> trigger());
        position = gameObject == null ? new Vector2() : gameObject.getPosition();
        size = gameObject == null ? Game.getSize() : gameObject.getSize();
    }

    @Override
    protected void start() {
        trigger();
    }

    @Override
    protected void update() {
        if (!position.equals(lastPosition) || !size.equals(lastSize)) {
            trigger();
        }
        lastPosition.set(position);
        lastSize.set(size);
    }

    @Override
    protected void onSizeChanged(float oldX, float oldY, float newX, float newY) {
        trigger();
    }

    public void setBounds(Bounds bounds) {
        position = bounds == null ? new Vector2() : bounds.getPosition();
        size = bounds == null ? Game.getSize() : bounds.getSize();
        if (super.getGameObject() != null) {
            trigger();
        }
    }

    public void setGameObject(GameObject gameObject) {
        position = gameObject == null ? new Vector2() : gameObject.getPosition();
        size = gameObject == null ? Game.getSize() : gameObject.getSize();
        if (super.getGameObject() != null) {
            trigger();
        }
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public Vector2 getOffset() {
        return offset;
    }

    public void trigger() {
        super.position.set(alignment.getPositionOnBounds(super.size, new Bounds(position, size)).plus(offset));
    }
}