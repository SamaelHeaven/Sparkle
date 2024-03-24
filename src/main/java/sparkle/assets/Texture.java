package sparkle.assets;

import sparkle.math.Vector2c;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public final class Texture {
    private final BufferedImage image;

    public Texture(URL url) {
        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException("Could not load texture: " + url.getPath(), e);
        }
    }

    public Texture(int width, int height) {
        this(new BufferedImage(Math.max(width, 1), Math.max(height, 1), BufferedImage.TYPE_INT_ARGB));
    }

    public Texture(BufferedImage image) {
        this.image = image;
    }

    public int getWidth() {
        if (image == null) {
            return 0;
        }
        return image.getWidth();
    }

    public int getHeight() {
        if (image == null) {
            return 0;
        }
        return image.getHeight();
    }

    public Vector2c getSize() {
        return new Vector2c(getWidth(), getHeight());
    }

    public BufferedImage getImage() {
        return image;
    }
}