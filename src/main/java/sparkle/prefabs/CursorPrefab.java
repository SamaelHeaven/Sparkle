package sparkle.prefabs;

import sparkle.components.CursorInitializer;
import sparkle.core.Cursor;
import sparkle.core.GameObject;
import sparkle.xml.XMLElement;
import sparkle.xml.XMLPrefab;

import java.util.Locale;

public final class CursorPrefab extends XMLPrefab {
    private Cursor cursor = Cursor.DEFAULT;

    @Override
    public GameObject instantiate() {
        var gameObject = super.getBlankObject();
        gameObject.addComponent(new CursorInitializer(cursor));
        return gameObject;
    }

    @Override
    protected String getTagName() {
        return "cursor";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {
        if (xmlElement.hasAttribute("cursor")) {
            cursor = Cursor.valueOf(xmlElement.getAttribute("cursor").trim().toUpperCase(Locale.ENGLISH));
            if (cursor == null) {
                cursor = new Cursor(xmlElement.getTexture("cursor"));
            }
        }
    }
}