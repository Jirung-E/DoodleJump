package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile;

import android.graphics.Rect;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.Sound;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.InGameScene;


public class BrokenTile extends Tile {
    protected static final Rect[] srcRect = {
        new Rect(0, 145, 124, 178),
        new Rect(0, 180, 124, 224),
        new Rect(0, 225, 124, 290),
        new Rect(0, 291, 124, 365),
    };
    private static final float ANIM_INTERVAL = 0.05f;
    private float dy;
    private float animTimer;
    private int index;


    public BrokenTile() {
        super();
    }

    public BrokenTile(float x, float y) {
        super(x, y);
    }


    @Override
    public void update() {
        if(collider != null) {
            if(!collider.isActive) {
                dy += InGameScene.GRAVITY * GameView.frameTime;
                y += dy * GameView.frameTime;
                animTimer += GameView.frameTime;
                index = 1 + (int)(animTimer / ANIM_INTERVAL);
                if(index >= srcRect.length) {
                    index = srcRect.length - 1;
                }
                setSrcRect();
            }
        }
        super.update();
    }


    public void trigger() {
        if(collider != null) {
            dy = 0.2f * InGameScene.GRAVITY;
            collider.isActive = false;
            index = 1;
            animTimer = 0;
            setSrcRect();
            Sound.playEffect(R.raw.tile_break);
        }
    }


    @Override
    protected Rect getSrcRect() {
        return srcRect[index];
    }


    @Override
    public void onRecycle() {
        super.onRecycle();
        collider.isActive = true;
        index = 0;
        setSrcRect();
    }
}
