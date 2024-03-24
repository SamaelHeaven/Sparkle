package sparkle.physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import sparkle.core.Component;
import sparkle.core.Game;
import sparkle.math.ObservableVector2;
import sparkle.math.Vector2;
import sparkle.math.Vector2Base;

import java.util.Objects;

public final class RigidBody extends Component {
    private final BodyType bodyType;
    private final Collider collider;
    private final ObservableVector2 linearVelocity;
    private Body body;
    private float angularVelocity;
    private float gravityScale;
    private float density;
    private float friction;
    private float restitution;
    private float rotation;
    private boolean fixedRotation;
    private boolean trigger;
    private boolean updating;

    public RigidBody(BodyType bodyType) {
        this(bodyType, null);
    }

    public RigidBody(BodyType bodyType, Collider collider) {
        this(bodyType, collider, 1, 1, 0, 0, false, false);
    }

    public RigidBody(BodyType bodyType, Collider collider, float gravityScale, float density, float friction, float restitution, boolean fixedRotation, boolean trigger) {
        this.bodyType = Objects.requireNonNullElse(bodyType, BodyType.DYNAMIC);
        this.collider = Objects.requireNonNullElse(collider, Collider.BOX);
        this.gravityScale = gravityScale;
        this.density = Math.max(0, density);
        this.friction = Math.max(0, friction);
        this.restitution = Math.max(0, restitution);
        this.fixedRotation = fixedRotation;
        this.trigger = trigger;
        linearVelocity = new ObservableVector2(this::onLinearVelocityChanged);
    }

    @Override
    protected void start() {
        if (super.getComponents(super.getClass()).size() > 1) {
            throw new IllegalStateException("Game object cannot have multiple bodies");
        }
        Game.getScene().getPhysics().addRigidBody(this);
    }

    @Override
    protected void destroy() {
        Game.getScene().getPhysics().removeRigidBody(this);
    }

    @Override
    protected void onPositionChanged(float oldX, float oldY, float newX, float newY) {
        if (body == null) {
            return;
        }
        if (!updating) {
            body.setTransform(new Vec2((newX + size.getX() / 2) * Physics.PIXELS_TO_METERS, (newY + size.getY() / 2) * Physics.PIXELS_TO_METERS), (float) Math.toRadians(rotation));
        }
    }

    @Override
    protected void onSizeChanged(float oldX, float oldY, float newX, float newY) {
        if (body == null) {
            return;
        }
        reset();
    }

    public void addForce(Vector2Base force) {
        if (body != null) {
            body.applyForce(new Vec2(force.getX(), force.getY()), body.getWorldCenter());
        }
    }

    public void addForce(Vector2Base force, Vector2Base point) {
        if (body != null) {
            body.applyForce(new Vec2(force.getX(), force.getY()), new Vec2(point.getX() * Physics.PIXELS_TO_METERS, point.getY() * Physics.PIXELS_TO_METERS));
        }
    }

    public void addLinearImpulse(Vector2Base impulse) {
        if (body != null) {
            body.applyLinearImpulse(new Vec2(impulse.getX(), impulse.getY()), body.getWorldCenter());
        }
    }

    public void addLinearImpulse(Vector2Base impulse, Vector2Base point) {
        if (body != null) {
            body.applyLinearImpulse(new Vec2(impulse.getX(), impulse.getY()), new Vec2(point.getX() * Physics.PIXELS_TO_METERS, point.getY() * Physics.PIXELS_TO_METERS));
        }
    }

    public void addAngularImpulse(float impulse) {
        body.applyAngularImpulse(impulse);
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        if (body != null) {
            body.setTransform(body.getPosition(), (float) Math.toRadians(rotation));
        }
    }

    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public Collider getCollider() {
        return collider;
    }

    public float getGravityScale() {
        return gravityScale;
    }

    public void setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
        if (body != null) {
            body.setGravityScale(gravityScale);
        }
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = Math.max(0, density);
        if (body != null) {
            var fixture = body.getFixtureList();
            while (fixture != null) {
                fixture.setDensity(this.density);
                fixture = fixture.getNext();
            }
        }
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = Math.max(0, friction);
        if (body != null) {
            var fixture = body.getFixtureList();
            while (fixture != null) {
                fixture.setFriction(this.friction);
                fixture = fixture.getNext();
            }
        }
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = Math.max(0, restitution);
        if (body != null) {
            var fixture = body.getFixtureList();
            while (fixture != null) {
                fixture.setRestitution(this.restitution);
                fixture = fixture.getNext();
            }
        }
    }

    public boolean isFixedRotation() {
        return fixedRotation;
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
        if (body != null) {
            body.setFixedRotation(fixedRotation);
        }
    }

    public boolean isTrigger() {
        return trigger;
    }

    public void setTrigger(boolean trigger) {
        this.trigger = trigger;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
        if (body != null) {
            body.setAngularVelocity(angularVelocity);
        }
    }

    void updateBody() {
        if (body == null) {
            return;
        }
        updating = true;
        linearVelocity.set(new Vector2(body.getLinearVelocity().x, body.getLinearVelocity().y).divide(Physics.PIXELS_TO_METERS));
        angularVelocity = body.getAngularVelocity();
        rotation = (float) Math.toDegrees(body.getAngle());
        super.position.set(body.getPosition().x / Physics.PIXELS_TO_METERS - super.size.getX() / 2, body.getPosition().y / Physics.PIXELS_TO_METERS - super.size.getY() / 2);
        updating = false;
    }

    void onContactBegin(RigidBody contact) {
        for (var component : super.getComponents(ContactListener.class)) {
            component.onContactBegin(contact.getGameObject());
        }
    }

    void onContactEnd(RigidBody contact) {
        for (var component : super.getComponents(ContactListener.class)) {
            component.onContactEnd(contact.getGameObject());
        }
    }

    boolean shouldCollide(RigidBody contact) {
        if (bodyType == BodyType.STATIC && contact.bodyType == BodyType.STATIC) {
            return false;
        }
        for (var component : super.getComponents(ContactListener.class)) {
            if (component.shouldCollide(contact.getGameObject())) {
                continue;
            }
            return false;
        }
        return true;
    }

    Body getBody() {
        return body;
    }

    void setBody(Body body) {
        this.body = body;
    }

    private void reset() {
        if (updating) {
            return;
        }
        if (body != null) {
            destroy();
            start();
        }
    }

    private void onLinearVelocityChanged(float oldX, float oldY, float newX, float newY) {
        if (body != null && !updating) {
            if (oldX != newX) {
                body.setLinearVelocity(new Vec2(newX * Physics.PIXELS_TO_METERS, body.getLinearVelocity().y));
            }
            if (oldY != newY) {
                body.setLinearVelocity(new Vec2(body.getLinearVelocity().x, newY * Physics.PIXELS_TO_METERS));
            }
        }
    }
}