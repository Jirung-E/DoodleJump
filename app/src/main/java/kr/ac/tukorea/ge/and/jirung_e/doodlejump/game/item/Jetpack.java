package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.item;

import android.graphics.Rect;

public class Jetpack extends Item {
    private static final String TAG = Item.class.getSimpleName();
    protected static final Rect[] srcRect = {
            new Rect(396, 529, 443, 602),
//            new Rect(807, 230, 843, 285),
    };
    public static final float BOOST_POWER = 2.0f;
    public static final float BOOST_TIME = 4.0f;


    @Override
    protected Rect getSrcRect() {
        return srcRect[0];
    }

    @Override
    protected float getSize() {
        return 0.5f;
    }
}
