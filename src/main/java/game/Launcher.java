package game;

import sparkle.core.Game;
import sparkle.core.GameConfig;
import sparkle.utils.ResourceLoader;
import sparkle.xml.XMLScene;

public final class Launcher {
    public static void main(String[] args) {
        var gameConfig = new GameConfig();
        var scene = new XMLScene(ResourceLoader.getXMLDocument("scene.xml"));
        Game.run(gameConfig, scene);
    }
}