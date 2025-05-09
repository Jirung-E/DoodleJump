package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile;

import android.graphics.Rect;


public class NormalTile extends Tile {
    protected static final Rect srcRect = new Rect(0, 0, 118, 33);


    public NormalTile() {
        super();
    }

    public NormalTile(float x, float y) {
        super(x, y);
    }


    @Override
    protected Rect getSrcRect() {
        return srcRect;
    }
}
