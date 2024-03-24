package sparkle.xml;

import sparkle.core.GameObject;

import java.net.URL;

public final class XMLLoaderPrefab extends XMLPrefab {
    private URL url;

    @Override
    public GameObject instantiate() {
        var gameObject = super.getBlankObject();
        if (url != null) {
            gameObject.addComponent(new XMLLoader(url));
        }
        return gameObject;
    }

    @Override
    protected String getTagName() {
        return "xmlLoader";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {
        url = xmlElement.getURL("url");
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}