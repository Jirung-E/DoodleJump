package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile;

import android.graphics.Canvas;
import android.graphics.Rect;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.ILayerProvider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IRecyclable;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.Sprite;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.physics.BoxCollider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.Layer;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.item.Item;


public abstract class Tile implements IGameObject, ILayerProvider<Layer>, IRecyclable {
    private static final String TAG = Tile.class.getSimpleName();
    public static final float DEFAULT_WIDTH = Metrics.width / 5.4f;
    public static final int PADDING_X = (int)(Metrics.width * 0.01f);   // 1%
    public static final int START_X = (int)DEFAULT_WIDTH/2 + PADDING_X;
    public static final int END_X = (int)(Metrics.width - DEFAULT_WIDTH/2) - PADDING_X;
    public static final int X_RANGE = END_X - START_X;
    protected Sprite sprite;
    public float x, y;
    public BoxCollider collider;
    protected float offsetY;
    public Item item;


    public Tile() {
        init(0, 0);
    }

    public Tile(float x, float y) {
        init(x, y);
    }


    protected void init(float x, float y) {
        this.x = x;
        this.y = y;

        sprite = new Sprite(R.mipmap.tiles);
        collider = new BoxCollider(0, 0);

        setSrcRect();
    }

    protected abstract Rect getSrcRect();
    protected void setSrcRect() {
        Rect srcRect = getSrcRect();
        float IMG_HEIGHT = DEFAULT_WIDTH * ((float) srcRect.height() / srcRect.width());
        offsetY = IMG_HEIGHT / 2;

        sprite.setSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
        sprite.setSize(DEFAULT_WIDTH, IMG_HEIGHT);
        sprite.setOffset(0.0f, offsetY);
        updateSprite();

        collider.setSize(DEFAULT_WIDTH, IMG_HEIGHT * 0.8f);
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
        if(GameView.drawsDebugStuffs) {
            collider.draw(canvas);
        }
    }

    protected void updateCollider() {
        collider.setPosition(x, y + offsetY);
    }

    protected void updateSprite() {
        sprite.setPosition(x, y);
    }

    @Override
    public Layer getLayer() {
        return Layer.tile;
    }

    @Override
    public void onRecycle() {
        item = null;
        collider.isActive = true;
    }
}
