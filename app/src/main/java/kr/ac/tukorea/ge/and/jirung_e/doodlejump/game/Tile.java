package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.BitmapPool;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.physics.BoxCollider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;


public class Tile implements IGameObject {
    private final RectF dstRect = new RectF();
    private static final Rect srcRect = new Rect(0, 0, 118, 33);
    private static final float IMG_WIDTH = Metrics.width / 5.4f;
    private static final float IMG_HEIGHT = IMG_WIDTH * ((float) srcRect.height() / srcRect.width());
    private float x, y;
    public BoxCollider collider;
    private final Bitmap bitmap;

    public Tile(float x, float y) {
        bitmap = BitmapPool.get(R.mipmap.tiles);

        this.x = x;
        this.y = y;
        collider = new BoxCollider(IMG_WIDTH, IMG_HEIGHT);

        updateCollider();
        updateRect();
    }


    @Override
    public void update() {
        updateCollider();
        updateRect();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
        collider.draw(canvas);
    }

    private void updateRect() {
        dstRect.set(x - IMG_WIDTH / 2, y - IMG_HEIGHT / 2,
                x + IMG_WIDTH / 2, y + IMG_HEIGHT / 2);
    }

    private void updateCollider() {
        collider.setPosition(x, y);
    }
}
