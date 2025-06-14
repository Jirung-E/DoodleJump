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
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.InGameScene;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.Layer;

public abstract class Monster implements IGameObject, ILayerProvider<Layer>, IRecyclable {
    private static final String TAG = Monster.class.getSimpleName();
    protected Sprite sprite;
    public float x, y;
    public float dx, dy;
    public BoxCollider collider;

    public Monster() {
        sprite = new Sprite(R.mipmap.tiles);
        collider = new BoxCollider(0, 0);

        setSrcRect();
    }

    @Override
    public void update() {
        // Path를 따라서 움직이도록 한다
        if(!collider.isActive) {
            dy += GameView.frameTime * InGameScene.GRAVITY;
        }
        x += dx * GameView.frameTime;
        y += dy * GameView.frameTime;
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
        collider.setPosition(x, y);
    }

    protected void updateSprite() {
        sprite.setPosition(x, y);
    }

    public void die() {
        collider.isActive = false;
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        if(GameView.drawsDebugStuffs) {
            collider.draw(canvas);
        }
    }

    @Override
    public Layer getLayer() {
        return Layer.enemy;
    }

    @Override
    public void onRecycle() {
        collider.isActive = true;
        dy = 0;
    }
}
