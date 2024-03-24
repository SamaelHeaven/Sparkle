package sparkle.xml;

import org.w3c.dom.Element;
import sparkle.assets.Font;
import sparkle.assets.Texture;
import sparkle.core.Game;
import sparkle.paints.Color;
import sparkle.utils.ResourceLoader;

import java.net.URL;
import java.util.*;

public final class XMLElement {
    private final Element element;

    XMLElement(Element element) {
        this.element = element;
    }

    public String getTagName() {
        return element.getTagName();
    }

    public boolean hasAttribute(String attribute) {
        return element.hasAttribute(attribute);
    }

    public String getAttribute(String attribute) {
        return element.getAttribute(attribute);
    }

    public Map<String, String> getAttributes() {
        var result = new HashMap<String, String>();
        var attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            var node = attributes.item(i);
            result.put(node.getNodeName(), node.getNodeValue());
        }
        return result;
    }

    public String getRawText() {
        return element.getTextContent();
    }

    public String getText() {
        return getRawText().replaceAll("(?m)^\\s+|\\s+$", "");
    }

    public List<XMLElement> getElementsByName(String name) {
        var result = new ArrayList<XMLElement>();
        var nodes = element.getElementsByTagName(name);
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i) instanceof Element e) {
                result.add(new XMLElement(e));
            }
        }
        return result;
    }

    public List<XMLElement> getChildElements() {
        var result = new ArrayList<XMLElement>();
        var nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i) instanceof Element e) {
                result.add(new XMLElement(e));
            }
        }
        return result;
    }

    public String getString(String attribute) {
        return getString(attribute, "");
    }

    public String getString(String attribute, String defaultValue) {
        if (!hasAttribute(attribute)) {
            return defaultValue;
        }
        return getAttribute(attribute).trim();
    }

    public float getNumber(String attribute) {
        return getNumber(attribute, 0);
    }

    public float getNumber(String attribute, float defaultValue) {
        if (!hasAttribute(attribute)) {
            return defaultValue;
        }
        var value = getAttribute(attribute).trim();
        if (value.equalsIgnoreCase("width")) {
            return Game.getWidth();
        }
        if (value.equalsIgnoreCase("height")) {
            return Game.getHeight();
        }
        return Float.parseFloat(value.replace("_", ""));
    }

    public boolean getBoolean(String attribute) {
        return getBoolean(attribute, false);
    }

    public boolean getBoolean(String attribute, boolean defaultValue) {
        if (!hasAttribute(attribute)) {
            return defaultValue;
        }
        return Boolean.parseBoolean(getAttribute(attribute).trim());
    }

    public URL getURL(String attribute) {
        return getURL(attribute, null);
    }

    public URL getURL(String attribute, URL defaultValue) {
        if (!hasAttribute(attribute)) {
            return defaultValue;
        }
        return ResourceLoader.getURL(getAttribute(attribute).trim());
    }

    public Color getColor(String attribute) {
        return getColor(attribute, null);
    }

    public Color getColor(String attribute, Color defaultValue) {
        if (!hasAttribute(attribute)) {
            return defaultValue;
        }
        var value = getAttribute(attribute).trim().toUpperCase(Locale.ENGLISH);
        var color = Color.valueOf(value);
        if (color != null) {
            return color;
        }
        return new Color(value);
    }

    public Font getFont(String attribute) {
        return getFont(attribute, null);
    }

    public Font getFont(String attribute, Font defaultValue) {
        return getFont(attribute, defaultValue, true);
    }

    public Font getFont(String attribute, Font defaultValue, boolean cache) {
        if (!hasAttribute(attribute)) {
            return defaultValue;
        }
        if (cache) {
            return ResourceLoader.getFont(getAttribute(attribute).trim());
        }
        return new Font(getURL(attribute));
    }

    public Texture getTexture(String attribute) {
        return getTexture(attribute, null);
    }

    public Texture getTexture(String attribute, Texture defaultValue) {
        return getTexture(attribute, defaultValue, true);
    }

    public Texture getTexture(String attribute, Texture defaultValue, boolean cache) {
        if (!hasAttribute(attribute)) {
            return defaultValue;
        }
        if (cache) {
            return ResourceLoader.getTexture(getAttribute(attribute).trim());
        }
        return new Texture(getURL(attribute));
    }

    public <T extends Enum<T>> T getEnum(Class<T> enumClass, String attribute) {
        return getEnum(enumClass, attribute, null);
    }

    public <T extends Enum<T>> T getEnum(Class<T> enumClass, String attribute, T defaultValue) {
        if (!hasAttribute(attribute)) {
            return defaultValue;
        }
        return Enum.valueOf(enumClass, getAttribute(attribute).trim().toUpperCase(Locale.ENGLISH));
    }

    void setAttribute(String attribute, String value) {
        element.setAttribute(attribute, value);
    }

    boolean hasChildNodes() {
        return element.hasChildNodes();
    }
}