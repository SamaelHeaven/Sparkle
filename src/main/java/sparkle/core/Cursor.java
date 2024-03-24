package sparkle.core;

import sparkle.assets.Texture;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Cursor {
    public static final Cursor CROSSHAIR = new Cursor("CROSSHAIR", java.awt.Cursor.CROSSHAIR_CURSOR);
    public static final Cursor DEFAULT = new Cursor("DEFAULT", java.awt.Cursor.DEFAULT_CURSOR);
    public static final Cursor E_RESIZE = new Cursor("E_RESIZE", java.awt.Cursor.E_RESIZE_CURSOR);
    public static final Cursor HAND = new Cursor("HAND", java.awt.Cursor.HAND_CURSOR);
    public static final Cursor MOVE = new Cursor("MOVE", java.awt.Cursor.MOVE_CURSOR);
    public static final Cursor N_RESIZE = new Cursor("N_RESIZE", java.awt.Cursor.N_RESIZE_CURSOR);
    public static final Cursor NE_RESIZE = new Cursor("NE_RESIZE", java.awt.Cursor.NE_RESIZE_CURSOR);
    public static final Cursor NW_RESIZE = new Cursor("NW_RESIZE", java.awt.Cursor.NW_RESIZE_CURSOR);
    public static final Cursor S_RESIZE = new Cursor("S_RESIZE", java.awt.Cursor.S_RESIZE_CURSOR);
    public static final Cursor SE_RESIZE = new Cursor("SE_RESIZE", java.awt.Cursor.SE_RESIZE_CURSOR);
    public static final Cursor SW_RESIZE = new Cursor("SW_RESIZE", java.awt.Cursor.SW_RESIZE_CURSOR);
    public static final Cursor TEXT = new Cursor("TEXT", java.awt.Cursor.TEXT_CURSOR);
    public static final Cursor W_RESIZE = new Cursor("W_RESIZE", java.awt.Cursor.W_RESIZE_CURSOR);
    public static final Cursor WAIT = new Cursor("WAIT", java.awt.Cursor.WAIT_CURSOR);
    public static final Cursor NONE = new Cursor("NONE", null);
    private static final List<Cursor> cursors = new ArrayList<>();

    static {
        cursors.add(CROSSHAIR);
        cursors.add(DEFAULT);
        cursors.add(E_RESIZE);
        cursors.add(HAND);
        cursors.add(MOVE);
        cursors.add(N_RESIZE);
        cursors.add(NE_RESIZE);
        cursors.add(NW_RESIZE);
        cursors.add(S_RESIZE);
        cursors.add(SE_RESIZE);
        cursors.add(SW_RESIZE);
        cursors.add(TEXT);
        cursors.add(W_RESIZE);
        cursors.add(WAIT);
        cursors.add(NONE);
    }

    private final java.awt.Cursor cursor;
    private final String name;

    public Cursor(Texture texture) {
        this(null, texture);
    }

    private Cursor(String name, Texture texture) {
        var cursorTexture = Objects.requireNonNullElse(texture, new Texture(1, 1));
        var toolkit = Toolkit.getDefaultToolkit();
        this.name = Objects.requireNonNullElse(name, super.toString());
        cursor = toolkit.createCustomCursor((cursorTexture.getImage() == null ? new Texture(1, 1) : cursorTexture).getImage(), new Point(), "");
    }

    private Cursor(String name, int code) {
        this.cursor = new java.awt.Cursor(code);
        this.name = name;
    }

    public static Cursor valueOf(String name) {
        for (var cursor : cursors) {
            if (cursor.name.equals(name)) {
                return cursor;
            }
        }
        return null;
    }

    public static Cursor[] values() {
        return cursors.toArray(new Cursor[0]);
    }

    @Override
    public String toString() {
        return name;
    }

    java.awt.Cursor getCursor() {
        return cursor;
    }
}