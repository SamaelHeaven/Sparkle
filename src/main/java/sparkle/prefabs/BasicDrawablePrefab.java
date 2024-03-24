package sparkle.prefabs;

import sparkle.drawables.StrokeType;
import sparkle.paints.Paint;
import sparkle.xml.XMLElement;

public abstract class BasicDrawablePrefab extends SpritePrefab {
    private Paint fill;
    private Paint stroke;
    private StrokeType strokeType;
    private float strokeWidth;
    private boolean asSprite;

    public BasicDrawablePrefab() {
        asSprite = true;
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {
        super.setProperties(xmlElement);
        fill = xmlElement.getColor("fill");
        stroke = xmlElement.getColor("stroke");
        strokeType = xmlElement.getEnum(StrokeType.class, "strokeType");
        strokeWidth = xmlElement.getNumber("strokeWidth");
        asSprite = xmlElement.getBoolean("asSprite", true);
    }

    public final Paint getFill() {
        return fill;
    }

    public final void setFill(Paint fill) {
        this.fill = fill;
    }

    public final Paint getStroke() {
        return stroke;
    }

    public final void setStroke(Paint stroke) {
        this.stroke = stroke;
    }

    public final StrokeType getStrokeType() {
        return strokeType;
    }

    public final void setStrokeType(StrokeType strokeType) {
        this.strokeType = strokeType;
    }

    public final float getStrokeWidth() {
        return strokeWidth;
    }

    public final void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public final boolean asSprite() {
        return asSprite;
    }

    public final void setAsSprite(boolean asSprite) {
        this.asSprite = asSprite;
    }
}