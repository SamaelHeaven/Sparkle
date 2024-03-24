package sparkle.core;

import sparkle.math.Bounds;
import sparkle.math.ObservableVector2;
import sparkle.math.Vector2;
import sparkle.math.Vector2Base;
import sparkle.physics.PhysicsCapabilities;

import java.util.*;

public final class GameObject extends PhysicsCapabilities implements Comparable<GameObject> {
    private final String name;
    private final Set<String> tags;
    private final List<Component> components;
    private final List<Component> componentsToAdd;
    private final List<Component> componentsToRemove;
    private final ObservableVector2 position;
    private final ObservableVector2 size;
    private int zIndex;
    private boolean cleaned;

    public GameObject(String name) {
        this(name, null, null, 0);
    }

    public GameObject(String name, Component... components) {
        this(name, null, null, 0);
        addComponents(components);
    }

    public GameObject(String name, Vector2Base position, Vector2Base size) {
        this(name, position, size, 0);
    }

    public GameObject(String name, Vector2Base position, Vector2Base size, int zIndex) {
        this.name = Objects.requireNonNullElse(name, "");
        this.position = new ObservableVector2(position, this::onPositionChanged);
        this.size = new ObservableVector2(size, this::onSizeChanged);
        this.zIndex = zIndex;
        tags = new HashSet<>();
        components = new ArrayList<>();
        componentsToAdd = new ArrayList<>();
        componentsToRemove = new ArrayList<>();
        cleaned = true;
    }

    public <T> T getComponent(Class<T> componentClass) {
        for (var component : components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                return componentClass.cast(component);
            }
        }
        return null;
    }

    public <T> List<T> getComponents(Class<T> componentClass) {
        var result = new ArrayList<T>();
        for (var component : components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                result.add(componentClass.cast(component));
            }
        }
        return result;
    }

    public void addComponent(Component component) {
        if (components.contains(component)) {
            return;
        }
        if (component.getGameObject() != null) {
            throw new RuntimeException("A component cannot be attached to multiple game objects");
        }
        componentsToAdd.add(component);
        cleaned = false;
        if (Game.getScene().isInitialized()) {
            return;
        }
        clean();
    }

    public void addComponents(Component... components) {
        for (var component : components) {
            addComponent(component);
        }
    }

    public void addComponents(Collection<Component> components) {
        for (var component : components) {
            addComponent(component);
        }
    }

    public void removeComponent(Component component) {
        componentsToRemove.add(component);
        cleaned = false;
        if (Game.getScene().isInitialized()) {
            return;
        }
        clean();
    }

    public <T> void removeComponents(Class<T> componentClass) {
        for (var component : components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                componentsToRemove.add(component);
            }
        }
        cleaned = false;
        if (Game.getScene().isInitialized()) {
            return;
        }
        clean();
    }

    public String getName() {
        return name;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }

    public List<Component> getComponents() {
        return Collections.unmodifiableList(components);
    }

    public Set<String> getTags() {
        return tags;
    }

    public int getZIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public Bounds getBounds() {
        return new Bounds(getPosition(), getSize());
    }

    @Override
    public int compareTo(GameObject other) {
        return Integer.compare(zIndex, other.zIndex);
    }

    @Override
    protected void fixedUpdate() {
        for (var component : getConcurrentSafeComponents()) {
            component.fixedUpdate();
        }
        clean();
    }

    void start() {
        for (var component : getConcurrentSafeComponents()) {
            component.start();
        }
        clean();
    }

    void update() {
        for (var component : getConcurrentSafeComponents()) {
            component.update();
        }
        clean();
    }

    void destroy() {
        for (var component : getConcurrentSafeComponents()) {
            component.destroy();
        }
        clean();
    }

    private void onPositionChanged(float oldX, float oldY, float newX, float newY) {
        for (var component : getConcurrentSafeComponents()) {
            component.onPositionChanged(oldX, oldY, newX, newY);
        }
    }

    private void onSizeChanged(float oldX, float oldY, float newX, float newY) {
        for (var component : getConcurrentSafeComponents()) {
            component.onSizeChanged(oldX, oldY, newX, newY);
        }
    }

    private void clean() {
        if (cleaned) {
            return;
        }
        do {
            cleaned = true;
            addPendingComponents();
            removePendingComponents();
        } while (!cleaned);
    }

    private void addPendingComponents() {
        var componentsToAdd = new ArrayList<>(this.componentsToAdd);
        components.addAll(componentsToAdd);
        for (var component : componentsToAdd) {
            component.setGameObject(this);
            if (Game.getScene().getGameObjects().contains(this)) {
                component.start();
            }
        }
        this.componentsToAdd.removeAll(componentsToAdd);
    }

    private void removePendingComponents() {
        var componentsToRemove = new ArrayList<>(this.componentsToRemove);
        components.removeAll(componentsToRemove);
        for (var component : componentsToRemove) {
            component.destroy();
            component.setGameObject(null);
        }
        this.componentsToRemove.removeAll(componentsToRemove);
    }

    private List<Component> getConcurrentSafeComponents() {
        return (Game.getScene().isInitialized() ? this.components : new ArrayList<>(this.components));
    }
}