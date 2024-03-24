package sparkle.components;

import sparkle.assets.Texture;
import sparkle.assets.TextureAtlas;
import sparkle.core.Component;
import sparkle.core.Game;
import sparkle.core.Time;
import sparkle.drawables.Sprite;

import java.util.Objects;

public final class Animation extends Component {
    public static final int INDEFINITE = -1;
    private final TextureAtlas textureAtlas;
    private final int beginIndex;
    private final int endIndex;
    private final int cycleCount;
    private float delay;
    private float timeLeft;
    private int index;
    private int cycle;
    private boolean destroyOnFinished;

    public Animation(TextureAtlas textureAtlas, float delay, int beginIndex, int endIndex, int cycleCount) {
        this.textureAtlas = Objects.requireNonNull(textureAtlas);
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        this.cycleCount = cycleCount;
        cycle = 0;
        timeLeft = delay;
        setDelay(delay);
        setIndex(beginIndex);
    }

    @Override
    protected void start() {
        reset();
    }

    @Override
    public void update() {
        if (cycle == cycleCount) {
            if (destroyOnFinished) {
                Game.getScene().removeGameObject(super.getGameObject());
            }
            return;
        }
        if (index > endIndex) {
            index = beginIndex;
        }
        timeLeft -= Time.getDelta();
        while (timeLeft <= 0) {
            timeLeft += delay;
            index++;
            if (index > endIndex) {
                cycle++;
                break;
            }
        }
        var sprite = super.getComponent(Sprite.class);
        if (sprite != null) {
            sprite.setTexture(getTexture());
        }
    }

    public void reset() {
        timeLeft = delay;
        index = beginIndex;
        cycle = 0;
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public Texture getTexture() {
        return textureAtlas.getTexture(getIndex());
    }

    public int getIndex() {
        return Math.min(index, endIndex);
    }

    public void setIndex(int index) {
        if (index < beginIndex || index > endIndex) {
            throw new IllegalArgumentException("The current index must be between the beginning and ending indices");
        }
        this.index = index;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        if (delay < 0) {
            throw new IllegalArgumentException("Delay must be greater than zero");
        }
        this.delay = delay;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public boolean isFinished() {
        return cycle == cycleCount;
    }

    public boolean isDestroyOnFinished() {
        return destroyOnFinished;
    }

    public void setDestroyOnFinished(boolean destroyOnFinished) {
        this.destroyOnFinished = destroyOnFinished;
    }
}