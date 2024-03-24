package sparkle.prefabs;

import sparkle.core.Drawable;
import sparkle.core.GameObject;
import sparkle.core.RenderingMode;
import sparkle.math.Vector2Base;
import sparkle.xml.XMLElement;
import sparkle.xml.XMLPrefab;

public abstract class DrawablePrefab extends XMLPrefab {
    private RenderingMode renderingMode;
    private Vector2Base anchor;

    @Override
    public final GameObject instantiate() {
        var gameObject = super.getBlankObject();
        var drawable = getDrawable(gameObject);
        drawable.setRenderingMode(renderingMode);
        drawable.setAnchor(anchor);
        gameObject.addComponent(drawable);
        return gameObject;
    }

    protected abstract Drawable getDrawable(GameObject gameObject);

    @Override
    protected void setProperties(XMLElement xmlElement) {
        renderingMode = xmlElement.getEnum(RenderingMode.class, "renderingMode");
    }

    public final RenderingMode getRenderingMode() {
        return renderingMode;
    }

    public final void setRenderingMode(RenderingMode renderingMode) {
        this.renderingMode = renderingMode;
    }

    public final Vector2Base getAnchor() {
        return anchor;
    }

    public final void setAnchor(Vector2Base anchor) {
        this.anchor = anchor;
    }
}