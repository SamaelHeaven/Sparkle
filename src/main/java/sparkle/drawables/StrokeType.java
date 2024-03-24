package sparkle.drawables;

import java.awt.*;

public enum StrokeType {
    SQUARED {
        @Override
        protected Stroke build(float width) {
            return new BasicStroke(width, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);
        }
    }, ROUNDED {
        @Override
        protected Stroke build(float width) {
            return new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        }
    };

    protected abstract Stroke build(float width);
}