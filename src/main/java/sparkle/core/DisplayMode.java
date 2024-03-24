package sparkle.core;

public enum DisplayMode {
    WINDOW {
        @Override
        protected void enable() {
            var frame = Game.getFrame();
            var device = Game.getDevice();
            if (device.getFullScreenWindow() != null) {
                device.setFullScreenWindow(null);
            }
            frame.setBounds(0, 0, Game.getWidth(), Game.getHeight());
            frame.setLocationRelativeTo(null);
            if (!frame.isVisible()) {
                frame.setVisible(true);
            }
        }
    }, MAXIMIZED {
        @Override
        protected void enable() {
            var frame = Game.getFrame();
            var device = Game.getDevice();
            var displayMode = device.getDisplayMode();
            var transform = device.getDefaultConfiguration().getDefaultTransform();
            if (device.getFullScreenWindow() != null) {
                device.setFullScreenWindow(null);
            }
            frame.setBounds(0, 0, (int) Math.round(displayMode.getWidth() * (1 / transform.getScaleX())), (int) Math.round(displayMode.getHeight() * (1 / transform.getScaleY())));
            frame.setLocationRelativeTo(null);
            if (!frame.isVisible()) {
                frame.setVisible(true);
            }
        }
    }, FULLSCREEN {
        @Override
        protected void enable() {
            var frame = Game.getFrame();
            var device = Game.getDevice();
            if (frame.isVisible()) {
                frame.setVisible(false);
            }
            device.setFullScreenWindow(frame);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    };

    public static boolean isFullscreenSupported() {
        return Game.getDevice().isFullScreenSupported();
    }

    protected abstract void enable();
}