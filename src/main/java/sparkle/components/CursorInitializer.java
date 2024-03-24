package sparkle.components;

import sparkle.core.Component;
import sparkle.core.Cursor;
import sparkle.core.Game;

public final class CursorInitializer extends Component {
    private final Cursor cursor;

    public CursorInitializer(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    protected void start() {
        Game.setCursor(cursor);
        var gameObject = super.getGameObject();
        super.removeComponent(this);
        if (gameObject.getComponents().size() == 1) {
            Game.getScene().removeGameObject(gameObject);
        }
    }
}