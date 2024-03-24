package sparkle.utils;

import sparkle.assets.Audio;
import sparkle.assets.Font;
import sparkle.assets.Texture;
import sparkle.xml.XMLDocument;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class ResourceLoader {
    private static final Map<String, Texture> textures = new HashMap<>();
    private static final Map<String, Font> fonts = new HashMap<>();

    public static URL getURL(String resourcePath) {
        return ResourceLoader.class.getResource(formatResourcePath(resourcePath));
    }

    public static Texture getTexture(String resourcePath) {
        if (textures.containsKey(resourcePath)) {
            return textures.get(resourcePath);
        }
        var texture = new Texture(getURL(resourcePath));
        textures.put(resourcePath, texture);
        return texture;
    }

    public static Font getFont(String resourcePath) {
        if (fonts.containsKey(resourcePath)) {
            return fonts.get(resourcePath);
        }
        var font = new Font(getURL(resourcePath));
        fonts.put(resourcePath, font);
        return font;
    }

    public static Audio getAudio(String resourcePath) {
        return new Audio(getURL(resourcePath));
    }

    public static Audio getAudio(String resourcePath, float volume) {
        return new Audio(getURL(resourcePath), volume);
    }

    public static Audio getAudio(String resourcePath, float volume, boolean loop) {
        return new Audio(getURL(resourcePath), volume, loop);
    }

    public static XMLDocument getXMLDocument(String resourcePath) {
        return new XMLDocument(getURL(resourcePath));
    }

    private static String formatResourcePath(String resourcePath) {
        if (!resourcePath.startsWith("/")) {
            return "/" + resourcePath;
        }
        return resourcePath;
    }
}