package sparkle.xml;

import sparkle.core.Component;
import sparkle.core.Game;

import java.net.URL;

public final class XMLLoader extends Component {
    private final URL url;

    public XMLLoader(URL url) {
        this.url = url;
    }

    @Override
    protected void start() {
        var document = new XMLDocument(url);
        Game.getScene().addGameObjects(document.instantiateGameObjects());
        var gameObject = super.getGameObject();
        super.removeComponent(this);
        if (gameObject.getComponents().size() == 1) {
            Game.getScene().removeGameObject(gameObject);
        }
    }
}