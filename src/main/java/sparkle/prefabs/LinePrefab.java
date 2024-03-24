package sparkle.prefabs;

import sparkle.core.Drawable;
import sparkle.core.GameObject;
import sparkle.drawables.Line;
import sparkle.drawables.StrokeType;
import sparkle.math.Vector2;
import sparkle.paints.Paint;
import sparkle.xml.XMLElement;

public final class LinePrefab extends DrawablePrefab {
    private final Vector2 startPosition;
    private final Vector2 endPosition;
    private Paint stroke;
    private StrokeType strokeType;
    private float strokeWidth;

    public LinePrefab() {
        startPosition = new Vector2();
        endPosition = new Vector2();
    }

    @Override
    protected String getTagName() {
        return "line";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {
        super.setProperties(xmlElement);
        startPosition.set(xmlElement.getNumber("startX"), xmlElement.getNumber("startY"));
        endPosition.set(xmlElement.getNumber("endX"), xmlElement.getNumber("endY"));
        stroke = xmlElement.getColor("stroke");
        strokeType = xmlElement.getEnum(StrokeType.class, "strokeType");
        strokeWidth = xmlElement.getNumber("strokeWidth");
    }

    @Override
    protected Drawable getDrawable(GameObject gameObject) {
        var line = new Line(null, null, stroke, strokeType, strokeWidth);
        line.getStartPosition().set(startPosition);
        line.getEndPosition().set(endPosition);
        return line;
    }

    public Vector2 getStartPosition() {
        return startPosition;
    }

    public Vector2 getEndPosition() {
        return endPosition;
    }

    public Paint getStroke() {
        return stroke;
    }

    public void setStroke(Paint stroke) {
        this.stroke = stroke;
    }

    public StrokeType getStrokeType() {
        return strokeType;
    }

    public void setStrokeType(StrokeType strokeType) {
        this.strokeType = strokeType;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }
}