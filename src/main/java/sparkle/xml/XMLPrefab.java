package sparkle.xml;

import sparkle.components.Aligner;
import sparkle.core.Component;
import sparkle.core.GameObject;
import sparkle.math.Alignment;
import sparkle.math.Bounds;
import sparkle.math.Vector2;
import sparkle.prefabs.Prefab;

import java.util.*;

public abstract class XMLPrefab extends Prefab {
    private static final Map<String, Map<String, String>> cachedAttributes = new HashMap<>();
    private final List<Component> components = new ArrayList<>();
    private Alignment alignment;

    protected abstract String getTagName();

    protected abstract void setProperties(XMLElement xmlElement);

    @Override
    protected final GameObject getBlankObject() {
        var gameObject = super.getBlankObject();
        if (alignment != null) {
            gameObject.addComponent(new Aligner(alignment, (Bounds) null, new Vector2(gameObject.getPosition().getX(), gameObject.getPosition().getY())));
        }
        gameObject.addComponents(components);
        components.clear();
        return gameObject;
    }

    void handleXMLElement(XMLElement xmlElement) {
        loadAttributes(xmlElement);
        loadComponents(xmlElement);
        super.setName(xmlElement.getString("name"));
        super.getPosition().set(xmlElement.getNumber("x"), xmlElement.getNumber("y"));
        super.getSize().set(xmlElement.getNumber("width"), xmlElement.getNumber("height"));
        super.setZIndex((int) xmlElement.getNumber("zIndex"));
        super.getTags().clear();
        super.getTags().addAll(Arrays.stream(xmlElement.getString("tags").split(",")).map(String::trim).toList());
        alignment = xmlElement.getEnum(Alignment.class, "alignment");
        setProperties(xmlElement);
    }

    private void loadAttributes(XMLElement xmlElement) {
        if (xmlElement.hasAttribute("attributes")) {
            getAttributes(xmlElement).forEach((attribute, value) -> {
                if (!xmlElement.hasAttribute(attribute)) {
                    xmlElement.setAttribute(attribute, value);
                }
            });
        }
    }

    private void loadComponents(XMLElement xmlElement) {
        if (!xmlElement.hasAttribute("components")) {
            return;
        }
        var componentsNames = xmlElement.getString("components").split(",");
        for (var componentName : componentsNames) {
            try {
                var componentClass = Class.forName(componentName.trim());
                var constructor = componentClass.getDeclaredConstructor();
                var instance = constructor.newInstance();
                if (instance instanceof Component component) {
                    components.add(component);
                    continue;
                }
                throw new RuntimeException("Object is not a Component instance");
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Could not instantiate component: " + componentName, e);
            }
        }
    }

    private Map<String, String> getAttributes(XMLElement xmlElement) {
        var url = xmlElement.getString("attributes");
        if (cachedAttributes.containsKey(url)) {
            return cachedAttributes.get(url);
        }
        var attributesDocument = new XMLDocument(xmlElement.getURL("attributes"));
        var attributes = attributesDocument.getRoot().getAttributes();
        cachedAttributes.put(url, attributes);
        return attributes;
    }
}