package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.ILayerProvider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.BitmapPool;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.Sprite;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.physics.BoxCollider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;


public class Tile implements IGameObject, ILayerProvider<InGameLayer> {
    private final RectF dstRect = new RectF();
    private static final Rect srcRect = new Rect(0, 0, 118, 33);
    private static final float IMG_WIDTH = Metrics.width / 5.4f;
    private static final float IMG_HEIGHT = IMG_WIDTH * ((float) srcRect.height() / srcRect.width());
    private float x, y;
    public BoxCollider collider;
    private Sprite sprite;

    public Tile(float x, float y) {
        sprite = new Sprite(R.mipmap.tiles);
        sprite.setSrcRect(0, 0, 118, 33);
        sprite.setSize(IMG_WIDTH, IMG_HEIGHT);
        updateSprite();

        this.x = x;
        this.y = y;
        collider = new BoxCollider(IMG_WIDTH, IMG_HEIGHT);
        updateCollider();
    }


    @Override
    public void update() {
        updateCollider();
        updateSprite();
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        collider.draw(canvas);
    }

    private void updateCollider() {
        collider.setPosition(x, y);
    }

    private void updateSprite() {
        sprite.setPosition(x, y);
    }

    @Override
    public InGameLayer getLayer() {
        return InGameLayer.tile;
    }
}
