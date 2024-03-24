package sparkle.core;

import sparkle.math.Camera;
import sparkle.math.Vector2;
import sparkle.physics.Physics;

import java.util.*;

public abstract class Scene implements Iterable<GameObject> {
    private final List<GameObject> gameObjects = new ArrayList<>();
    private final List<GameObject> gameObjectsToAdd = new ArrayList<>();
    private final List<GameObject> gameObjectsToRemove = new ArrayList<>();
    private final Camera camera = new Camera();
    private final Physics physics = new Physics(new Vector2(0, 9.807f));
    private boolean cleaned = true;
    private boolean initialized = false;

    protected abstract void initialize();

    protected abstract void update();

    @Override
    public Iterator<GameObject> iterator() {
        return getGameObjects().iterator();
    }

    public List<GameObject> getGameObjects() {
        return Collections.unmodifiableList(gameObjects);
    }

    public GameObject getGameObjectByName(String name) {
        for (var gameObject : gameObjects) {
            if (gameObject.getName().equals(name)) {
                return gameObject;
            }
        }
        return null;
    }

    public List<GameObject> getGameObjectsByName(String name) {
        List<GameObject> gameObjects = new ArrayList<>();
        for (var gameObject : this.gameObjects) {
            if (gameObject.getName().equals(name)) {
                gameObjects.add(gameObject);
            }
        }
        return gameObjects;
    }

    public GameObject getGameObjectByTag(String tag) {
        for (var gameObject : gameObjects) {
            if (gameObject.getTags().contains(tag)) {
                return gameObject;
            }
        }
        return null;
    }

    public List<GameObject> getGameObjectsByTag(String tag) {
        List<GameObject> gameObjects = new ArrayList<>();
        for (var gameObject : this.gameObjects) {
            if (gameObject.getTags().contains(tag)) {
                gameObjects.add(gameObject);
            }
        }
        return gameObjects;
    }

    public <T> T getGameObjectByType(Class<T> componentClass) {
        for (var gameObject : gameObjects) {
            var component = gameObject.getComponent(componentClass);
            if (component != null) {
                return component;
            }
        }
        return null;
    }

    public <T> List<T> getGameObjectsByType(Class<T> componentClass) {
        List<T> gameObjects = new ArrayList<>();
        for (var gameObject : this.gameObjects) {
            var component = gameObject.getComponent(componentClass);
            if (component != null) {
                gameObjects.add(component);
            }
        }
        return gameObjects;
    }

    public void addGameObject(GameObject gameObject) {
        if (gameObjects.contains(gameObject)) {
            return;
        }
        if (!initialized) {
            gameObjects.add(gameObject);
            gameObject.start();
            return;
        }
        gameObjectsToAdd.add(gameObject);
        cleaned = false;
    }

    public void addGameObjects(Collection<GameObject> gameObjects) {
        if (initialized) {
            gameObjectsToAdd.addAll(gameObjects);
            cleaned = false;
            return;
        }
        this.gameObjects.addAll(gameObjects);
        for (var gameObject : gameObjects) {
            gameObject.start();
        }
    }

    public void removeGameObject(GameObject gameObject) {
        if (!initialized) {
            gameObjects.remove(gameObject);
            gameObject.destroy();
            return;
        }
        gameObjectsToRemove.add(gameObject);
        cleaned = false;
    }

    public void removeGameObjects(Collection<GameObject> gameObjects) {
        for (var gameObject : gameObjects) {
            removeGameObject(gameObject);
        }
    }

    public void updateState() {
        clean();
        Collections.sort(gameObjects);
        for (var gameObject : gameObjects) {
            gameObject.update();
        }
        physics.update();
    }

    public Camera getCamera() {
        return camera;
    }

    public Physics getPhysics() {
        return physics;
    }

    void start() {
        if (initialized) {
            return;
        }
        initialize();
        initialized = true;
    }

    boolean isInitialized() {
        return initialized;
    }

    private void clean() {
        if (cleaned) {
            return;
        }
        do {
            cleaned = true;
            addPendingGameObjects();
            removePendingGameObjects();
        } while (!cleaned);
    }

    private void addPendingGameObjects() {
        var gameObjectsToAdd = new ArrayList<>(this.gameObjectsToAdd);
        gameObjects.addAll(gameObjectsToAdd);
        for (var gameObject : gameObjectsToAdd) {
            gameObject.start();
        }
        this.gameObjectsToAdd.removeAll(gameObjectsToAdd);
    }

    private void removePendingGameObjects() {
        var gameObjectsToRemove = new ArrayList<>(this.gameObjectsToRemove);
        gameObjects.removeAll(gameObjectsToRemove);
        for (var gameObject : gameObjectsToRemove) {
            gameObject.destroy();
        }
        this.gameObjectsToRemove.removeAll(gameObjectsToRemove);
    }
}