package sparkle.physics;

import sparkle.core.GameObject;

public interface ContactListener {
    default void onContactBegin(GameObject contact) {}

    default void onContactEnd(GameObject contact) {}

    default boolean shouldCollide(GameObject contact) {
        return true;
    }
}