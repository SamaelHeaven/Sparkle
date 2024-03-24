package sparkle.physics;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import sparkle.core.Game;
import sparkle.core.Time;
import sparkle.math.ObservableVector2;
import sparkle.math.Vector2;
import sparkle.math.Vector2Base;
import sparkle.math.Vector2c;

public final class Physics {
    static final float PIXELS_TO_METERS = 0.05f;
    private final static int VELOCITY_ITERATIONS = 6;
    private final static int POSITION_ITERATIONS = 2;
    private final World world;
    private final ObservableVector2 gravity;
    private float time = 0;

    public Physics(Vector2Base gravity) {
        world = new World(new Vec2());
        world.setContactListener(new ContactListener());
        world.setContactFilter(new ContactFilter());
        this.gravity = new ObservableVector2(this::onGravityChanged);
        this.gravity.set(gravity);
    }

    public void update() {
        var fixedDelta = Time.getFixedDelta();
        for (time += Time.getDelta(); time >= fixedDelta; time -= fixedDelta) {
            world.step(fixedDelta, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            var body = world.getBodyList();
            while (body != null) {
                var rigidBody = (RigidBody) body.getUserData();
                rigidBody.updateBody();
                body = body.getNext();
            }
            for (var gameObject : Game.getScene().getGameObjects()) {
                ((PhysicsCapabilities) gameObject).fixedUpdate();
            }
        }
    }

    public void rayCast(RayCastCallback rayCastCallback, Vector2Base pointA, Vector2Base pointB) {
        world.raycast(new RayCast(rayCastCallback), new Vec2(pointA.getX() * PIXELS_TO_METERS, pointA.getY() * PIXELS_TO_METERS), new Vec2(pointB.getX() * PIXELS_TO_METERS, pointB.getY() * PIXELS_TO_METERS));
    }

    public Vector2 getGravity() {
        return gravity;
    }

    void addRigidBody(RigidBody rigidBody) {
        var body = world.createBody(getBodyDef(rigidBody));
        body.setUserData(rigidBody);
        body.createFixture(getFixtureDef(rigidBody));
        body.setLinearVelocity(new Vec2(rigidBody.getLinearVelocity().getX() * PIXELS_TO_METERS, rigidBody.getLinearVelocity().getY() * PIXELS_TO_METERS));
        body.setAngularVelocity(rigidBody.getAngularVelocity());
        body.setTransform(body.getPosition(), (float) Math.toRadians(rigidBody.getRotation()));
        rigidBody.setBody(body);
    }

    void removeRigidBody(RigidBody rigidBody) {
        var body = rigidBody.getBody();
        if (body != null) {
            world.destroyBody(body);
            rigidBody.setBody(null);
        }
    }

    private void onGravityChanged(float oldX, float oldY, float newX, float newY) {
        world.setGravity(new Vec2(newX, newY));
    }

    private BodyDef getBodyDef(RigidBody rigidBody) {
        var position = rigidBody.getGameObject().getPosition();
        var size = rigidBody.getGameObject().getSize();
        var bodyDef = new BodyDef();
        bodyDef.fixedRotation = rigidBody.isFixedRotation();
        bodyDef.allowSleep = false;
        bodyDef.bullet = true;
        bodyDef.gravityScale = rigidBody.getGravityScale();
        bodyDef.type = rigidBody.getBodyType().bodyType;
        bodyDef.position.set((position.getX() + size.getX() / 2) * PIXELS_TO_METERS, (position.getY() + size.getY() / 2) * PIXELS_TO_METERS);
        return bodyDef;
    }

    private FixtureDef getFixtureDef(RigidBody rigidBody) {
        var fixtureDef = new FixtureDef();
        fixtureDef.isSensor = rigidBody.isTrigger();
        fixtureDef.shape = rigidBody.getCollider().buildShape(rigidBody.getGameObject().getSize());
        fixtureDef.density = rigidBody.getDensity();
        fixtureDef.friction = rigidBody.getFriction();
        fixtureDef.restitution = rigidBody.getRestitution();
        return fixtureDef;
    }

    private static final class ContactListener implements org.jbox2d.callbacks.ContactListener {
        @Override
        public void beginContact(Contact contact) {
            var bA = (RigidBody) contact.getFixtureA().getBody().getUserData();
            var bB = (RigidBody) contact.getFixtureB().getBody().getUserData();
            if (!bA.shouldCollide(bB) || !bB.shouldCollide(bA)) {
                return;
            }
            bA.onContactBegin(bB);
            bB.onContactBegin(bA);
        }

        @Override
        public void endContact(Contact contact) {
            var bA = (RigidBody) contact.getFixtureA().getBody().getUserData();
            var bB = (RigidBody) contact.getFixtureB().getBody().getUserData();
            if (!bA.shouldCollide(bB) || !bB.shouldCollide(bA)) {
                return;
            }
            bA.onContactEnd(bB);
            bB.onContactEnd(bA);
        }

        @Override
        public void preSolve(Contact contact, Manifold manifold) {}

        @Override
        public void postSolve(Contact contact, ContactImpulse contactImpulse) {}
    }

    private static final class ContactFilter extends org.jbox2d.callbacks.ContactFilter {
        @Override
        public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
            var bA = (RigidBody) fixtureA.getBody().getUserData();
            var bB = (RigidBody) fixtureB.getBody().getUserData();
            if (bA.shouldCollide(bB) && bB.shouldCollide(bA)) {
                return super.shouldCollide(fixtureA, fixtureB);
            }
            return false;
        }
    }

    private record RayCast(RayCastCallback callback) implements org.jbox2d.callbacks.RayCastCallback {
        @Override
        public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
            var gameObject = ((RigidBody) fixture.getBody().getUserData()).getGameObject();
            var pt = new Vector2c(point.x / PIXELS_TO_METERS, point.y / PIXELS_TO_METERS);
            var nl = new Vector2c(normal.x, normal.y);
            return callback.reportRayCast(gameObject, pt, nl, fraction);
        }
    }
}