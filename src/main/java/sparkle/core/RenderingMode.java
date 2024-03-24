package sparkle.core;

public enum RenderingMode {
    WORLD {
        @Override
        public float getModifierX(float x) {
            return x - Game.getScene().getCamera().getX();
        }

        @Override
        public float getModifierY(float y) {
            return y - Game.getScene().getCamera().getY();
        }
    }, SCREEN {
        @Override
        public float getModifierX(float x) {
            return x;
        }

        @Override
        public float getModifierY(float y) {
            return y;
        }
    };

    public abstract float getModifierX(float x);

    public abstract float getModifierY(float y);
}