package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.item;

import android.graphics.Rect;

public class Propeller extends Item {
    private static final String TAG = Propeller.class.getSimpleName();
    protected static final Rect srcRect = new Rect(663, 472, 726, 511);


    @Override
    public ItemId getId() {
        return ItemId.PROPELLER;
    }


    @Override
    protected Rect getSrcRect() {
        return srcRect;
    }

    @Override
    protected float getSize() {
        return 0.65f;
    }
}
