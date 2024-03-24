package sparkle.assets;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public final class Font {
    private final java.awt.Font font;

    public Font(URL url) {
        try (var inputStream = url.openStream()) {
            font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, inputStream);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException("Could not load font", e);
        }
    }

    public Font(java.awt.Font font) {
        this.font = font;
    }

    public java.awt.Font getFont() {
        return font;
    }
}