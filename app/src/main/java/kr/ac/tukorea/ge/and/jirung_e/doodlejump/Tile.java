package kr.ac.tukorea.ge.and.jirung_e.doodlejump;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public class Tile {
    private final RectF dstRect = new RectF();
    private static final Rect srcRect = new Rect(0, 0, 78, 22);
    private static final float IMG_WIDTH = 2.0f;
    private static final float IMG_HEIGHT = IMG_WIDTH * ((float) srcRect.height() / srcRect.width());
    private float x, y;
    public BoxCollider collider;


    public Tile(float x, float y) {
        this.x = x;
        this.y = y;
        collider = new BoxCollider(IMG_WIDTH, IMG_HEIGHT);
        updateCollider();
        updateRect();
    }


    private static Bitmap bitmap;
    public static void setBitmap(Bitmap bitmap) {
        Tile.bitmap = bitmap;
    }


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
