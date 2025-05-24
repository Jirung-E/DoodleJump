package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.item;

import android.graphics.Rect;

public class Propeller extends Booster {
    private static final String TAG = Propeller.class.getSimpleName();
    protected static final Rect srcRect = new Rect(663, 472, 726, 511);


    @Override
    protected Rect getSrcRect() {
        return srcRect;
    }

    @Override
    protected float getSize() {
        return 0.65f;
    }

    @Override
    public float getBoostPower() {
        return 1.5f;
    }

    @Override
    public float getBoostTime() {
        return 4.0f;
    }
}
