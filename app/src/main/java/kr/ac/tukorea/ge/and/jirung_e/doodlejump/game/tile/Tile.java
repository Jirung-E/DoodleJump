package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.ILayerProvider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IRecyclable;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.Sprite;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.physics.BoxCollider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.InGameLayer;


public abstract class Tile implements IGameObject, ILayerProvider<InGameLayer>, IRecyclable {
    protected static final float DEFAULT_WIDTH = Metrics.width / 5.4f;
    public static final int PADDING_X = (int)(Metrics.width * 0.01f);   // 1%
    public static final int START_X = (int)Tile.DEFAULT_WIDTH/2 + PADDING_X;
    public static final int END_X = (int)(Metrics.width - Tile.DEFAULT_WIDTH/2) - PADDING_X;
    public static final int X_RANGE = END_X - START_X;
    protected Sprite sprite;
    public float x, y;
    public BoxCollider collider;


    public Tile() {
        init(0, 0);
    }

    public Tile(float x, float y) {
        init(x, y);
    }


    protected void init(float x, float y) {
        sprite = new Sprite(R.mipmap.tiles);

        Rect srcRect = getSrcRect();
        float IMG_HEIGHT = DEFAULT_WIDTH * ((float) srcRect.height() / srcRect.width());


        sprite.setSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
        sprite.setSize(DEFAULT_WIDTH, IMG_HEIGHT);
        updateSprite();

        this.x = x;
        this.y = y;
        collider = new BoxCollider(DEFAULT_WIDTH, IMG_HEIGHT);
        updateCollider();
    }

    protected abstract Rect getSrcRect();


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

    protected void updateCollider() {
        collider.setPosition(x, y);
    }

    protected void updateSprite() {
        sprite.setPosition(x, y);
    }

    @Override
    public InGameLayer getLayer() {
        return InGameLayer.tile;
    }

    @Override
    public void onRecycle() {

    }
}
