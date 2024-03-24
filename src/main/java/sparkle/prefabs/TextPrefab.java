package sparkle.prefabs;

import sparkle.assets.Font;
import sparkle.core.Drawable;
import sparkle.core.GameObject;
import sparkle.drawables.Text;
import sparkle.xml.XMLElement;

public final class TextPrefab extends BasicDrawablePrefab {
    private Font font;
    private String text;
    private float fontSize;

    public TextPrefab() {
        fontSize = Text.DEFAULT_FONT_SIZE;
    }

    @Override
    protected String getTagName() {
        return "text";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {
        super.setProperties(xmlElement);
        var cacheFont = xmlElement.getBoolean("cacheFont", true);
        font = xmlElement.getFont("font", null, cacheFont);
        text = xmlElement.getText();
        fontSize = xmlElement.getNumber("fontSize", Text.DEFAULT_FONT_SIZE);
    }

    @Override
    protected Drawable getDrawable(GameObject gameObject) {
        var text = new Text(gameObject.getPosition(), font, super.getFill(), super.getStroke(), super.getStrokeType(), this.text, fontSize, super.getStrokeWidth(), super.getRotation());
        if (super.asSprite()) {
            var texture = text.toTexture();
            super.setTexture(texture);
            return super.getDrawable(gameObject);
        }
        gameObject.getSize().set(text.getSize());
        return text;
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
}