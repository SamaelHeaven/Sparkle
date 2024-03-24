package sparkle.assets;

import sparkle.math.Vector2c;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TextureAtlas {
    private final List<Texture> textures;
    private final float textureWidth;
    private final float textureHeight;

    public TextureAtlas(Texture texture, int nbRows, int nbCols) {
        this(texture, (texture.getWidth() / (float) nbCols), (texture.getHeight() / (float) nbRows), nbRows * nbCols, 0);
    }

    public TextureAtlas(Texture texture, float textureWidth, float textureHeight, int nbTexture, int spacing) {
        textures = new ArrayList<>();
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        loadTextures(Objects.requireNonNull(texture), nbTexture, spacing);
    }

    public Texture getTexture(int index) {
        return textures.get(index);
    }

    public float getTextureWidth() {
        return textureWidth;
    }

    public float getTextureHeight() {
        return textureHeight;
    }

    public Vector2c getTextureSize() {
        return new Vector2c(textureWidth, textureHeight);
    }

    private void loadTextures(Texture texture, int nbTexture, int spacing) {
        var offsetX = 0.0;
        var offsetY = 0.0;
        var image = texture.getImage();
        for (var i = 0; i < nbTexture; i++) {
            var newTexture = new Texture(image.getSubimage((int) offsetX, (int) offsetY, (int) textureWidth, (int) textureHeight));
            textures.add(newTexture);
            offsetX += textureWidth + spacing;
            if (offsetX + textureWidth > texture.getWidth()) {
                offsetX = 0;
                offsetY += textureHeight + spacing;
            }
        }
    }
}