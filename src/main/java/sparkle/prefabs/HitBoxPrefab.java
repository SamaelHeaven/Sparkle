package sparkle.prefabs;

import sparkle.components.HitBox;
import sparkle.core.GameObject;
import sparkle.physics.BodyType;
import sparkle.physics.Collider;
import sparkle.physics.RigidBody;

public final class HitBoxPrefab extends Prefab {
    private GameObject gameObject;
    private HitBox.Type type;
    private float boxSize;
    private boolean ignoreTrigger;

    public HitBoxPrefab() {
        boxSize = 1;
        ignoreTrigger = true;
    }

    @Override
    public GameObject instantiate() {
        var gameObject = super.getBlankObject();
        gameObject.setZIndex(this.gameObject.getZIndex() + 1);
        gameObject.addComponent(new RigidBody(BodyType.DYNAMIC, Collider.BOX, 0, 0, 0, 0, true, true));
        gameObject.addComponent(new HitBox(this.gameObject, type, boxSize, ignoreTrigger));
        return gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public HitBox.Type getType() {
        return type;
    }

    public void setType(HitBox.Type type) {
        this.type = type;
    }

    public float getBoxSize() {
        return boxSize;
    }

    public void setBoxSize(float boxSize) {
        this.boxSize = boxSize;
    }

    public boolean isIgnoreTrigger() {
        return ignoreTrigger;
    }

    public void setIgnoreTrigger(boolean ignoreTrigger) {
        this.ignoreTrigger = ignoreTrigger;
    }
}