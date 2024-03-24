package sparkle.prefabs;

import sparkle.assets.Audio;
import sparkle.components.AudioSource;
import sparkle.core.GameObject;
import sparkle.xml.XMLElement;
import sparkle.xml.XMLPrefab;

import java.net.URL;

public final class AudioPrefab extends XMLPrefab {
    private URL url;
    private float volume;
    private boolean loop;
    private boolean playOnStart;

    @Override
    public GameObject instantiate() {
        var gameObject = super.getBlankObject();
        if (url != null) {
            gameObject.addComponent(new AudioSource(new Audio(url, volume, loop), playOnStart));
        }
        return gameObject;
    }

    @Override
    protected String getTagName() {
        return "audio";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {
        url = xmlElement.getURL("url");
        volume = xmlElement.getNumber("volume", 1);
        loop = xmlElement.getBoolean("loop");
        playOnStart = xmlElement.getBoolean("playOnStart", true);
    }
}