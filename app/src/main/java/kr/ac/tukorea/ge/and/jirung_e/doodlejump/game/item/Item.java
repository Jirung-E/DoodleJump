package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.item;

import android.graphics.Canvas;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.ILayerProvider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IRecyclable;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.physics.BoxCollider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.Sprite;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.InGameLayer;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile.Tile;

public abstract class Item implements IGameObject, ILayerProvider<InGameLayer>, IRecyclable {
    private static final String TAG = Tile.class.getSimpleName();
    protected Sprite sprite;
    public float x, y;
    public BoxCollider collider;
    protected float offsetY;
    protected Tile parent;


    public Item() {
        init(null);
    }

    public Item(@NonNull Tile parent) {
        init(parent);
    }

    protected void init(Tile parent) {
        this.parent = parent;

        sprite = new Sprite(R.mipmap.tiles);
        collider = new BoxCollider(0, 0);

        setSrcRect();
    }

    protected abstract Rect getSrcRect();
    protected void setSrcRect() {
        // 타일 사이즈를 기준으로 아이템 사이즈 설정
        float WIDTH = Tile.DEFAULT_WIDTH * getSize();

        Rect srcRect = getSrcRect();
        float IMG_HEIGHT = WIDTH * ((float) srcRect.height() / srcRect.width());
        offsetY = -IMG_HEIGHT / 3;

        sprite.setSrcRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);
        sprite.setSize(WIDTH, IMG_HEIGHT);
        sprite.setOffset(0.0f, offsetY);
        updateSprite();

        collider.setSize(WIDTH, IMG_HEIGHT);
        updateCollider();
    }
    protected abstract float getSize();


    public void setParent(Tile parent) {
        if(parent == null) {
            if(this.parent != null) {
                this.parent.item = null;
            }
            this.parent = null;
            return;
        }

        this.parent = parent;
        this.parent.item = this;
    }


    @Override
    public void update() {
        updateCollider();
        updateSprite();
    }

    protected void updateCollider() {
        float x = this.x;
        float y = this.y;
        if (parent != null) {
            x += parent.x;
            y += parent.y;
        }
        collider.setPosition(x, y + offsetY);
    }

    protected void updateSprite() {
        float x = this.x;
        float y = this.y;
        if (parent != null) {
            x += parent.x;
            y += parent.y;
        }
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
        return InGameLayer.item;
    }

    @Override
    public void onRecycle() {
        parent = null;
    }
}
