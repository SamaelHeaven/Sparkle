package sparkle.xml;

import sparkle.prefabs.*;

import java.util.HashMap;

public final class XMLPrefabRepository {
    private static final HashMap<String, XMLPrefab> prefabs = new HashMap<>();

    static {
        addPrefab(new AudioPrefab());
        addPrefab(new BarPrefab());
        addPrefab(new BlockadePrefab());
        addPrefab(new CursorPrefab());
        addPrefab(new LinePrefab());
        addPrefab(new OvalPrefab());
        addPrefab(new RectanglePrefab());
        addPrefab(new SpritePrefab());
        addPrefab(new TextPrefab());
        addPrefab(new XMLLoaderPrefab());
    }

    public static XMLPrefab getPrefab(String tagName) {
        return prefabs.get(tagName);
    }

    public static void addPrefab(XMLPrefab prefab) {
        var tagName = prefab.getTagName();
        if (prefabs.containsKey(tagName)) {
            throw new IllegalArgumentException("A prefab with the same tag name (" + tagName + ") already exists");
        }
        prefabs.put(tagName, prefab);
    }
}