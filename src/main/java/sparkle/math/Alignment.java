package sparkle.math;

public enum Alignment {
    TOP_LEFT {
        @Override
        public Vector2Base getPositionOnBounds(Vector2Base size, Bounds otherBounds) {
            return otherBounds.getPosition();
        }
    }, TOP_CENTER {
        @Override
        public Vector2Base getPositionOnBounds(Vector2Base size, Bounds otherBounds) {
            return otherBounds.getPosition().plus((otherBounds.getWidth() - size.getX()) / 2, 0);
        }
    }, TOP_RIGHT {
        @Override
        public Vector2Base getPositionOnBounds(Vector2Base size, Bounds otherBounds) {
            return otherBounds.getPosition().plus(otherBounds.getWidth() - size.getX(), 0);
        }
    }, CENTER_LEFT {
        @Override
        public Vector2Base getPositionOnBounds(Vector2Base size, Bounds otherBounds) {
            return otherBounds.getPosition().plus(0, (otherBounds.getHeight() - size.getY()) / 2);
        }
    }, CENTER {
        @Override
        public Vector2Base getPositionOnBounds(Vector2Base size, Bounds otherBounds) {
            return otherBounds.getPosition().plus(otherBounds.getSize().minus(size).divide(2));
        }
    }, CENTER_RIGHT {
        @Override
        public Vector2Base getPositionOnBounds(Vector2Base size, Bounds otherBounds) {
            return otherBounds.getPosition().plus(otherBounds.getWidth() - size.getX(), (otherBounds.getHeight() - size.getY()) / 2);
        }
    }, BOTTOM_LEFT {
        @Override
        public Vector2Base getPositionOnBounds(Vector2Base size, Bounds otherBounds) {
            return otherBounds.getPosition().plus(0, otherBounds.getHeight() - size.getY());
        }
    }, BOTTOM_CENTER {
        @Override
        public Vector2Base getPositionOnBounds(Vector2Base size, Bounds otherBounds) {
            return otherBounds.getPosition().plus((otherBounds.getWidth() - size.getX()) / 2, otherBounds.getHeight() - size.getY());
        }
    }, BOTTOM_RIGHT {
        @Override
        public Vector2Base getPositionOnBounds(Vector2Base size, Bounds otherBounds) {
            return otherBounds.getPosition().plus(otherBounds.getSize().minus(size));
        }
    };

    public abstract Vector2Base getPositionOnBounds(Vector2Base size, Bounds bounds);
}