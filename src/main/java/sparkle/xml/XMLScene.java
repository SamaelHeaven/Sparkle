package sparkle.xml;

import sparkle.core.Scene;

import java.util.Objects;

public class XMLScene extends Scene {
    private final XMLDocument xmlDocument;

    public XMLScene(XMLDocument xmlDocument) {
        this.xmlDocument = Objects.requireNonNull(xmlDocument);
    }

    @Override
    protected void initialize() {
        super.addGameObjects(xmlDocument.instantiateGameObjects());
    }

    @Override
    protected void update() {
        super.updateState();
    }
}