package sparkle.xml;

import sparkle.components.Aligner;
import sparkle.core.GameObject;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class XMLDocument {
    private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private static final DocumentBuilder builder;

    static {
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private final XMLElement root;

    public XMLDocument(URL url) {
        try (var inputStream = url.openStream()) {
            var document = builder.parse(inputStream);
            root = new XMLElement(document.getDocumentElement());
        } catch (Exception e) {
            throw new RuntimeException("Could not load xml document: " + url.getPath(), e);
        }
    }

    public XMLElement getRoot() {
        return root;
    }

    public List<GameObject> instantiateGameObjects() {
        var result = new ArrayList<GameObject>();
        instantiateGameObjects(result, null, root);
        return result;
    }

    private void instantiateGameObjects(List<GameObject> gameObjects, GameObject parent, XMLElement element) {
        for (var child : element.getChildElements()) {
            var prefab = XMLPrefabRepository.getPrefab(child.getTagName());
            GameObject newParent = null;
            if (prefab != null) {
                prefab.handleXMLElement(child);
                newParent = prefab.instantiate();
                if (parent != null) {
                    var aligner = newParent.getComponent(Aligner.class);
                    if (aligner != null) {
                        aligner.setGameObject(parent);
                    }
                }
                gameObjects.add(newParent);
            }
            if (child.hasChildNodes()) {
                instantiateGameObjects(gameObjects, newParent, child);
            }
        }
    }
}