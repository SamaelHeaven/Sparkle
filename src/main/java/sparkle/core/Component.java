package sparkle.core;

import sparkle.math.Vector2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Component {
    protected Vector2 position;
    protected Vector2 size;
    private GameObject gameObject;

    protected void start() {}

    protected void update() {}

    protected void fixedUpdate() {}

    protected void destroy() {}

    protected void onPositionChanged(float oldX, float oldY, float newX, float newY) {}

    protected void onSizeChanged(float oldX, float oldY, float newX, float newY) {}

    protected <T> T getComponent(Class<T> componentClass) {
        if (gameObject == null) {
            return null;
        }
        return gameObject.getComponent(componentClass);
    }

    protected <T> List<T> getComponents(Class<T> componentClass) {
        if (gameObject == null) {
            return new ArrayList<>();
        }
        return gameObject.getComponents(componentClass);
    }

    protected void addComponent(Component component) {
        if (gameObject == null) {
            return;
        }
        gameObject.addComponent(component);
    }

    protected void addComponents(Collection<Component> components) {
        if (gameObject == null) {
            return;
        }
        gameObject.addComponents(components);
    }

    protected void removeComponent(Component component) {
        if (gameObject == null) {
            return;
        }
        gameObject.removeComponent(component);
    }

    protected <T> void removeComponents(Class<T> componentClass) {
        if (gameObject == null) {
            return;
        }
        gameObject.removeComponents(componentClass);
    }

    protected List<Component> getComponents() {
        if (gameObject == null) {
            return new ArrayList<>();
        }
        return gameObject.getComponents();
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
        position = null;
        size = null;
        if (gameObject != null) {
            position = gameObject.getPosition();
            size = gameObject.getSize();
        }
    }
}