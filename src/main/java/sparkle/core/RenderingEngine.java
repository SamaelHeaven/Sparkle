package sparkle.core;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;

public final class RenderingEngine {
    private final BufferStrategy bufferStrategy;
    private final RenderingHints renderingHints;
    private VolatileImage imageBuffer;
    private Graphics2D graphicsBuffer;

    RenderingEngine() {
        var frame = Game.getFrame();
        frame.createBufferStrategy(3);
        bufferStrategy = frame.getBufferStrategy();
        renderingHints = buildRenderingHints();
    }

    public void initialize() {
        var graphicsConfiguration = Game.getGraphicsConfiguration();
        imageBuffer = graphicsConfiguration.createCompatibleVolatileImage(Game.getWidth(), Game.getHeight());
    }

    public Graphics2D buildGraphics() {
        graphicsBuffer = imageBuffer.createGraphics();
        graphicsBuffer.setRenderingHints(renderingHints);
        return graphicsBuffer;
    }

    public void renderScreen() {
        if (graphicsBuffer != null) {
            graphicsBuffer.dispose();
        }
        Graphics2D graphics = null;
        do {
            try {
                graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
                drawOnGraphics(graphics);
            } finally {
                if (graphics != null) {
                    graphics.dispose();
                    graphics = null;
                }
            }
            bufferStrategy.show();
        } while (bufferStrategy.contentsLost());
    }

    private void drawOnGraphics(Graphics2D graphics) {
        var graphicsConfiguration = Game.getGraphicsConfiguration();
        if (imageBuffer.validate(graphicsConfiguration) == VolatileImage.IMAGE_INCOMPATIBLE) {
            imageBuffer.flush();
            imageBuffer = graphicsConfiguration.createCompatibleVolatileImage(Game.getWidth(), Game.getHeight());
            return;
        }
        var windowSize = Game.getActualSize();
        var scaleFactor = Game.getScaleFactor();
        var size = Game.getSize().multiply(scaleFactor);
        var position = windowSize.minus(size).divide(2);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, Math.round(windowSize.getX()), Math.round(position.getY()));
        graphics.fillRect(0, Math.round(position.getY() + size.getY()), Math.round(windowSize.getX()), Math.round(position.getY()));
        graphics.fillRect(0, 0, Math.round(position.getX()), Math.round(windowSize.getY()));
        graphics.fillRect(Math.round(position.getX() + size.getX()), 0, Math.round(position.getX()), Math.round(windowSize.getY()));
        graphics.drawImage(imageBuffer, Math.round(position.getX()), Math.round(position.getY()), Math.round(size.getX()), Math.round(size.getY()), null);
    }

    private RenderingHints buildRenderingHints() {
        var hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return hints;
    }
}