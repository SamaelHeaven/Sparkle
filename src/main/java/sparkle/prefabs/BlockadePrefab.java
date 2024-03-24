package sparkle.prefabs;

import sparkle.assets.Texture;
import sparkle.core.GameObject;
import sparkle.drawables.Oval;
import sparkle.drawables.Rectangle;
import sparkle.drawables.Sprite;
import sparkle.paints.Color;
import sparkle.physics.BodyType;
import sparkle.physics.Collider;
import sparkle.physics.RigidBody;
import sparkle.xml.XMLElement;
import sparkle.xml.XMLPrefab;

public final class BlockadePrefab extends XMLPrefab {
    private Color color;
    private Collider collider;

    @Override
    public GameObject instantiate() {
        var gameObject = super.getBlankObject();
        gameObject.addComponent(getRigidBody());
        if (color != null && !color.equals(Color.TRANSPARENT)) {
            gameObject.addComponent(getSprite(gameObject));
        }
        return gameObject;
    }

    @Override
    protected String getTagName() {
        return "blockade";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {
        color = xmlElement.getColor("color");
        collider = xmlElement.getEnum(Collider.class, "collider");
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Collider getCollider() {
        return collider;
    }

    public void setCollider(Collider collider) {
        this.collider = collider;
    }

    private Sprite getSprite(GameObject gameObject) {
        Texture texture = null;
        if (collider == null || collider == Collider.BOX) {
            texture = new Rectangle(null, gameObject.getSize(), color).toTexture();
        } else if (collider == Collider.CIRCLE) {
            texture = new Oval(null, gameObject.getSize(), color).toTexture();
        }
        return new Sprite(gameObject.getPosition(), gameObject.getSize(), texture);
    }

    private RigidBody getRigidBody() {
        return new RigidBody(BodyType.STATIC, collider);
    }
}