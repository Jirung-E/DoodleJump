package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.item;

import android.graphics.Rect;

import androidx.annotation.NonNull;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile.Tile;

public class Spring extends Item {
    private static final String TAG = Spring.class.getSimpleName();
    protected static final Rect[] srcRect = {
            new Rect(807, 198, 843, 221),
            new Rect(807, 230, 843, 285),
    };
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

    public void trigger() {
        if(collider != null) {
            collider.isActive = false;
            setSrcRect();
        }
    }

    @Override
    protected Rect getSrcRect() {
        if(collider == null) {
            return srcRect[0];
        }

        if(collider.isActive) {
            return srcRect[0];
        }
        else {
            return srcRect[1];
        }
    }

    @Override
    protected float getSize() {
        return 0.3f;
    }

    @Override
    public void onRecycle() {
        super.onRecycle();
        collider.isActive = true;
        setSrcRect();
    }
}
