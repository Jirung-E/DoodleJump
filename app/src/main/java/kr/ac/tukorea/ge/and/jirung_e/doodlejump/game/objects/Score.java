package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.BitmapPool;


public class Score implements IGameObject {
    private final Bitmap bitmap = BitmapPool.get(R.mipmap.top_score);
    private final Rect[] srcRects = {
            new Rect(877, 0, 904, 34),
            new Rect(640, 0, 654, 34),
            new Rect(654, 0, 685, 34),
            new Rect(687, 0, 713, 34),
            new Rect(713, 0, 736, 34),
            new Rect(737, 0, 766, 34),
            new Rect(767, 0, 796, 34),
            new Rect(797, 0, 825, 34),
            new Rect(824, 0, 850, 34),
            new Rect(852, 0, 876, 34),
    };
    private final RectF dstRect = new RectF();
    private float score = 0;
    public float left;
    public float baseY;
    public float height;


    public Score(float left, float baseY, float height) {
        this.left = left;
        this.baseY = baseY;
        this.height = height;
    }


    public void setScore(int score) {
        this.score = score;
    }
    public float getScore() {
        return score;
    }

    public void add(float amount) {
        score += amount;
    }


    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        int value = (int)score;
        int divisor = 1;
        while(value / divisor >= 10) {
            divisor *= 10;
        }

        float x = left;
        float top = baseY - height;
        do {
            int digit = value / divisor;
            Rect srcRect = srcRects[digit];
            float width = srcRect.width() * height / srcRect.height();
            dstRect.set(x, top, x + width, baseY);
            canvas.drawBitmap(bitmap, srcRect, dstRect, null);
            x += width;
            value %= divisor;
            divisor /= 10;
        } while(divisor > 0);
    }
}
