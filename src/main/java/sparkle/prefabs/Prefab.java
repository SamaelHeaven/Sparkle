package sparkle.prefabs;

import sparkle.core.GameObject;
import sparkle.math.Vector2;

import java.util.HashSet;
import java.util.Set;

public abstract class Prefab {
    private final Vector2 position = new Vector2();
    private final Vector2 size = new Vector2();
    private final Set<String> tags = new HashSet<>();
    private String name = "";
    private int zIndex;

    public abstract GameObject instantiate();

    public final Vector2 getPosition() {
        return position;
    }

    public final Vector2 getSize() {
        return size;
    }

    public final Set<String> getTags() {
        return tags;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final int getZIndex() {
        return zIndex;
    }

    public final void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    protected GameObject getBlankObject() {
        var gameObject = new GameObject(name);
        gameObject.getPosition().set(position);
        gameObject.getSize().set(size);
        gameObject.setZIndex(zIndex);
        gameObject.getTags().addAll(tags);
        return gameObject;
    }
}