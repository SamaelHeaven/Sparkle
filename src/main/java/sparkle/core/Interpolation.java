package sparkle.core;

import java.awt.*;

public enum Interpolation {
    BILINEAR(RenderingHints.VALUE_INTERPOLATION_BILINEAR),
    BICUBIC(RenderingHints.VALUE_INTERPOLATION_BICUBIC),
    NEAREST_NEIGHBOR(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

    static final RenderingHints.Key key = RenderingHints.KEY_INTERPOLATION;
    public final Object value;

    Interpolation(Object value) {
        this.value = value;
    }
}