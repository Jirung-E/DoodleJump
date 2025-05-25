package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.item;

import android.graphics.Rect;

public class Jetpack extends Item {
    private static final String TAG = Jetpack.class.getSimpleName();
    protected static final Rect srcRect = new Rect(396, 529, 443, 602);


    @Override
    public ItemId getId() {
        return ItemId.JETPACK;
    }


    @Override
    protected Rect getSrcRect() {
        return srcRect;
    }

    @Override
    protected float getSize() {
        return 0.5f;
    }
}
