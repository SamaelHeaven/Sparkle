package sparkle.components;

import sparkle.core.Component;
import sparkle.core.Game;
import sparkle.core.GameObject;
import sparkle.physics.ContactListener;
import sparkle.physics.RigidBody;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class HitBox extends Component implements ContactListener {
    private final Set<GameObject> colliders;
    private final Set<GameObject> deadColliders;
    private final GameObject gameObject;
    private final Type type;
    private final float boxSize;
    private final boolean ignoreTrigger;

    public HitBox(GameObject gameObject, Type type) {
        this(gameObject, type, 1);
    }

    public HitBox(GameObject gameObject, Type type, float boxSize) {
        this(gameObject, type, boxSize, true);
    }

    public HitBox(GameObject gameObject, Type type, float boxSize, boolean ignoreTrigger) {
        this.gameObject = Objects.requireNonNull(gameObject);
        this.type = Objects.requireNonNull(type);
        this.boxSize = boxSize;
        this.ignoreTrigger = ignoreTrigger;
        colliders = new HashSet<>();
        deadColliders = new HashSet<>();
    }

    @Override
    protected void start() {
        type.setPosition(this);
        type.setSize(this);
    }

    @Override
    protected void fixedUpdate() {
        type.setPosition(this);
        for (var collider : colliders) {
            if (!Game.getScene().getGameObjects().contains(collider)) {
                deadColliders.add(collider);
            }
        }
        colliders.removeAll(deadColliders);
        deadColliders.clear();
    }

    @Override
    public boolean shouldCollide(GameObject contact) {
        if (contact == super.getGameObject()) {
            return false;
        }
        if (contact == gameObject) {
            return false;
        }
        var body = contact.getComponent(RigidBody.class);
        if ((ignoreTrigger && body != null && body.isTrigger())) {
            return false;
        }
        for (var hitBox : contact.getComponents(HitBox.class)) {
            if (hitBox.gameObject == gameObject) {
                return false;
            }
        }
        return ContactListener.super.shouldCollide(contact);
    }

    @Override
    public void onContactBegin(GameObject contact) {
        colliders.add(contact);
    }

    @Override
    public void onContactEnd(GameObject contact) {
        colliders.remove(contact);
    }

    public Set<GameObject> getColliders() {
        return Collections.unmodifiableSet(colliders);
    }

    public Type getType() {
        return type;
    }

    public GameObject getBoxGameObject() {
        return gameObject;
    }

    public float getBoxSize() {
        return boxSize;
    }

    public boolean isIgnoringTrigger() {
        return ignoreTrigger;
    }

    public enum Type {
        LEFT {
            @Override
            protected void setPosition(HitBox hitBox) {
                hitBox.position.set(hitBox.gameObject.getPosition().translateX(-hitBox.boxSize));
            }

            @Override
            protected void setSize(HitBox hitBox) {
                hitBox.size.set(hitBox.gameObject.getSize().multiply(0, 1).translateX(hitBox.boxSize));
            }
        }, RIGHT {
            @Override
            protected void setPosition(HitBox hitBox) {
                hitBox.position.set(hitBox.gameObject.getPosition().translateX(hitBox.gameObject.getSize().getX()));
            }

            @Override
            protected void setSize(HitBox hitBox) {
                hitBox.size.set(hitBox.gameObject.getSize().multiply(0, 1).translateX(hitBox.boxSize));
            }
        }, TOP {
            @Override
            protected void setPosition(HitBox hitBox) {
                hitBox.position.set(hitBox.gameObject.getPosition().translateY(-hitBox.boxSize));
            }

            @Override
            protected void setSize(HitBox hitBox) {
                hitBox.size.set(hitBox.gameObject.getSize().multiply(1, 0).translateY(hitBox.boxSize));
            }
        }, BOTTOM {
            @Override
            protected void setPosition(HitBox hitBox) {
                hitBox.position.set(hitBox.gameObject.getPosition().translateY(hitBox.gameObject.getSize().getY()));
            }

            @Override
            protected void setSize(HitBox hitBox) {
                hitBox.size.set(hitBox.gameObject.getSize().multiply(1, 0).translateY(hitBox.boxSize));
            }
        };

        protected abstract void setPosition(HitBox hitBox);

        protected abstract void setSize(HitBox hitBox);
    }
}