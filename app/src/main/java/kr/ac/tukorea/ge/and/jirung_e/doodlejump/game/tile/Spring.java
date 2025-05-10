package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile;

import android.graphics.Rect;
import android.util.Log;

import androidx.annotation.NonNull;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.InGameLayer;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.Item;

public class Spring extends Item {
    private static final String TAG = Spring.class.getSimpleName();
    protected static final Rect srcRect = new Rect(807, 198, 843, 221);
    private Tile parent;


    public Spring() {
        init(null);
    }

    public Spring(@NonNull Tile parent) {
        init(parent);
    }


    @Override
    public void update() {
        if (parent != null) {
            this.x = parent.x;
            this.y = parent.y;
        }
        super.update();
    }

    @Override
    protected Rect getSrcRect() {
        return srcRect;
    }

    @Override
    protected float getSize() {
        return 0.3f;
    }

    @Override
    public void onRecycle() {
        super.onRecycle();
        collider.isActive = true;
    }
}
