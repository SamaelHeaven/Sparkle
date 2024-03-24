package sparkle.physics;

public enum BodyType {
    STATIC(org.jbox2d.dynamics.BodyType.STATIC),
    KINEMATIC(org.jbox2d.dynamics.BodyType.KINEMATIC),
    DYNAMIC(org.jbox2d.dynamics.BodyType.DYNAMIC);

    final org.jbox2d.dynamics.BodyType bodyType;

    BodyType(org.jbox2d.dynamics.BodyType bodyType) {
        this.bodyType = bodyType;
    }
}