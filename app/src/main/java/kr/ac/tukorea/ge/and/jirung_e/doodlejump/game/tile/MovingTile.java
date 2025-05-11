package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile;

import android.graphics.Rect;

import java.util.Random;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;

public class MovingTile extends Tile {
    protected static final Rect srcRect = new Rect(0, 34, 118, 67);
    private static final float SPEED = srcRect.width();
    private int dx;


    public MovingTile() {
        super();
        dx = new Random().nextInt(2) * 2 - 1; // -1 or 1
    }

    public MovingTile(float x, float y) {
        super(x, y);
        dx = new Random().nextInt(2) * 2 - 1; // -1 or 1
    }


    @Override
    public void update() {
        x += SPEED * dx * GameView.frameTime;
        if (x >= Tile.END_X) {
            x -= (x - Tile.END_X);
            dx = -1;
        } else if (x <= Tile.START_X) {
            x -= (x - Tile.START_X);
            dx = 1;
        }
        super.update();
    }


    @Override
    protected Rect getSrcRect() {
        return srcRect;
    }
}
