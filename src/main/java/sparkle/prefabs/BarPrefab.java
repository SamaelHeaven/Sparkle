package sparkle.prefabs;

import sparkle.assets.Font;
import sparkle.components.Bar;
import sparkle.core.GameObject;
import sparkle.core.Interpolation;
import sparkle.core.RenderingMode;
import sparkle.drawables.StrokeType;
import sparkle.drawables.Text;
import sparkle.math.Alignment;
import sparkle.paints.Color;
import sparkle.paints.Paint;
import sparkle.xml.XMLElement;
import sparkle.xml.XMLPrefab;

public final class BarPrefab extends XMLPrefab {
    private Alignment textAlignment;
    private Interpolation interpolation;
    private Color color;
    private Paint background;
    private Paint foreground;
    private Paint stroke;
    private Paint textFill;
    private Paint textStroke;
    private StrokeType strokeType;
    private StrokeType textStrokeType;
    private RenderingMode renderingMode;
    private Bar.FillDirection fillDirection;
    private Font font;
    private String text;
    private float fontSize;
    private float alpha;
    private float blur;
    private float rotation;
    private float value;
    private float strokeWidth;
    private float textStrokeWidth;
    private int radius;
    private boolean flippedHorizontally;
    private boolean flippedVertically;

    public BarPrefab() {
        alpha = 1;
        value = 1;
        strokeWidth = 1;
        radius = 10;
        fontSize = Text.DEFAULT_FONT_SIZE;
        background = Color.GRAY;
        foreground = Color.WHITE;
        stroke = Color.BLACK;
        textFill = Color.BLACK;
        interpolation = Interpolation.BILINEAR;
    }

    @Override
    public GameObject instantiate() {
        var gameObject = super.getBlankObject();
        var bar = new Bar(gameObject.getPosition(), gameObject.getSize());
        bar.setTextAlignment(textAlignment);
        bar.setInterpolation(interpolation);
        bar.setColor(color);
        bar.setBackground(background);
        bar.setForeground(foreground);
        bar.setStroke(stroke);
        bar.setTextFill(textFill);
        bar.setTextStroke(textStroke);
        bar.setStrokeType(strokeType);
        bar.setTextStrokeType(textStrokeType);
        bar.setRenderingMode(renderingMode);
        bar.setFillDirection(fillDirection);
        bar.setFont(font);
        bar.setText(text);
        bar.setFontSize(fontSize);
        bar.setAlpha(alpha);
        bar.setBlur(blur);
        bar.setRotation(rotation);
        bar.setValue(value);
        bar.setStrokeWidth(strokeWidth);
        bar.setTextStrokeWidth(textStrokeWidth);
        bar.setRadius(radius);
        bar.setFlippedHorizontally(flippedHorizontally);
        bar.setFlippedVertically(flippedVertically);
        gameObject.addComponent(bar);
        return gameObject;
    }

    @Override
    protected String getTagName() {
        return "bar";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {
        textAlignment = xmlElement.getEnum(Alignment.class, "textAlignment");
        interpolation = xmlElement.getEnum(Interpolation.class, "interpolation", Interpolation.BILINEAR);
        color = xmlElement.getColor("color");
        background = xmlElement.getColor("background", Color.GRAY);
        foreground = xmlElement.getColor("foreground", Color.WHITE);
        stroke = xmlElement.getColor("stroke", Color.BLACK);
        textFill = xmlElement.getColor("textFill", Color.BLACK);
        textStroke = xmlElement.getColor("textStroke");
        strokeType = xmlElement.getEnum(StrokeType.class, "strokeType");
        textStrokeType = xmlElement.getEnum(StrokeType.class, "textStrokeType");
        renderingMode = xmlElement.getEnum(RenderingMode.class, "renderingMode");
        fillDirection = xmlElement.getEnum(Bar.FillDirection.class, "fillDirection");
        var cacheFont = xmlElement.getBoolean("cacheFont", true);
        font = xmlElement.getFont("font", null, cacheFont);
        text = xmlElement.getText();
        fontSize = xmlElement.getNumber("fontSize", Text.DEFAULT_FONT_SIZE);
        alpha = xmlElement.getNumber("alpha", 1);
        blur = xmlElement.getNumber("blur");
        rotation = xmlElement.getNumber("rotation");
        value = xmlElement.getNumber("value", 1);
        strokeWidth = xmlElement.getNumber("strokeWidth", 1);
        textStrokeWidth = xmlElement.getNumber("textStrokeWidth");
        radius = (int) xmlElement.getNumber("radius", 10);
        flippedHorizontally = xmlElement.getBoolean("flippedHorizontally");
        flippedVertically = xmlElement.getBoolean("flippedVertically");
    }

    public Alignment getTextAlignment() {
        return textAlignment;
    }

    public void setTextAlignment(Alignment textAlignment) {
        this.textAlignment = textAlignment;
    }

    public Interpolation getInterpolation() {
        return interpolation;
    }

    public void setInterpolation(Interpolation interpolation) {
        this.interpolation = interpolation;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Paint getBackground() {
        return background;
    }

    public void setBackground(Paint background) {
        this.background = background;
    }

    public Paint getForeground() {
        return foreground;
    }

    public void setForeground(Paint foreground) {
        this.foreground = foreground;
    }

    public Paint getStroke() {
        return stroke;
    }

    public void setStroke(Paint stroke) {
        this.stroke = stroke;
    }

    public Paint getTextFill() {
        return textFill;
    }

    public void setTextFill(Paint textFill) {
        this.textFill = textFill;
    }

    public Paint getTextStroke() {
        return textStroke;
    }

    public void setTextStroke(Paint textStroke) {
        this.textStroke = textStroke;
    }

    public StrokeType getStrokeType() {
        return strokeType;
    }

    public void setStrokeType(StrokeType strokeType) {
        this.strokeType = strokeType;
    }

    public StrokeType getTextStrokeType() {
        return textStrokeType;
    }

    public void setTextStrokeType(StrokeType textStrokeType) {
        this.textStrokeType = textStrokeType;
    }

    public RenderingMode getRenderingMode() {
        return renderingMode;
    }

    public void setRenderingMode(RenderingMode renderingMode) {
        this.renderingMode = renderingMode;
    }

    public Bar.FillDirection getFillDirection() {
        return fillDirection;
    }

    public void setFillDirection(Bar.FillDirection fillDirection) {
        this.fillDirection = fillDirection;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getBlur() {
        return blur;
    }

    public void setBlur(float blur) {
        this.blur = blur;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public float getTextStrokeWidth() {
        return textStrokeWidth;
    }

    public void setTextStrokeWidth(float textStrokeWidth) {
        this.textStrokeWidth = textStrokeWidth;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public boolean isFlippedHorizontally() {
        return flippedHorizontally;
    }

    public void setFlippedHorizontally(boolean flippedHorizontally) {
        this.flippedHorizontally = flippedHorizontally;
    }

    public boolean isFlippedVertically() {
        return flippedVertically;
    }

    public void setFlippedVertically(boolean flippedVertically) {
        this.flippedVertically = flippedVertically;
    }
}