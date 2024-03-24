package sparkle.paints;

import sparkle.assets.Texture;
import sparkle.math.Bounds;
import sparkle.math.Vector2;

import java.awt.*;
import java.util.Objects;

public final class Surface implements Paint {
    private final TexturePaint paint;

    public Surface(Texture texture) {
        this(texture, null);
    }

    public Surface(Texture texture, Bounds anchor) {
        var bounds = Objects.requireNonNullElse(anchor, new Bounds(new Vector2(), (texture == null ? new Vector2() : texture.getSize())));
        paint = new TexturePaint((texture == null ? null : texture.getImage()), new Rectangle((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getX(), (int) bounds.getY()));
    }

    @Override
    public java.awt.Paint getPaint() {
        return paint;
    }
}