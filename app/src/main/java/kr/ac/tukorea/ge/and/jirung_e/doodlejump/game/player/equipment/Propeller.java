package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player.equipment;

import android.graphics.Canvas;
import android.graphics.Rect;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.Sprite;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player.Action;

public class Propeller extends Equipment {
    private final Sprite sprite;
    private static final Rect[] srcRect = {
        new Rect(0, 0, 64, 64),
        new Rect(64, 0, 128, 64),
        new Rect(0, 64, 64, 128),
        new Rect(64, 64, 128, 128)
    };
    private static final int[] nextFrame = {
        1, 2, 1, 3,
    };


    public Propeller() {
        sprite = new Sprite(kr.ac.tukorea.ge.and.jirung_e.doodlejump.R.mipmap.propeller);
        sprite.setOffset(-0.5f, 0.3f);
    }


    @Override
    public void update(float x, float y, Action action, float deltaTime) {

    }


    @Override
    public void draw(Canvas canvas) {

    }
}
