package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.monster;

import android.graphics.Canvas;
import android.graphics.Rect;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.ILayerProvider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IRecyclable;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.physics.BoxCollider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.Sprite;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.InGameLayer;

public abstract class Monster implements IGameObject, ILayerProvider<InGameLayer>, IRecyclable {
    private static final String TAG = Monster.class.getSimpleName();
    protected Sprite sprite;
    public float x, y;
    public BoxCollider collider;

    public Monster() {
        sprite = new Sprite(R.mipmap.tiles);
        collider = new BoxCollider(0, 0);

        setSrcRect();
    }

    @Override
    public void update() {
        // Path를 따라서 움직이도록 한다
        updateCollider();
        updateSprite();
    }

    protected abstract Rect getSrcRect();
    protected void setSrcRect() {
        float WIDTH = getSize();

        Rect srcRect = getSrcRect();
        float IMG_HEIGHT = WIDTH * ((float) srcRect.height() / srcRect.width());

        sprite.setSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
        sprite.setSize(WIDTH, IMG_HEIGHT);
        updateSprite();

        collider.setSize(WIDTH, IMG_HEIGHT);
        updateCollider();
    }
    protected abstract float getSize();

    protected void updateCollider() {
        float x = this.x;
        float y = this.y;
        collider.setPosition(x, y);
    }

    protected void updateSprite() {
        float x = this.x;
        float y = this.y;
        sprite.setPosition(x, y);
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        if(GameView.drawsDebugStuffs) {
            collider.draw(canvas);
        }
    }

    @Override
    public InGameLayer getLayer() {
        return InGameLayer.enemy;
    }

    @Override
    public void onRecycle() {
        collider.isActive = true;
    }
}
