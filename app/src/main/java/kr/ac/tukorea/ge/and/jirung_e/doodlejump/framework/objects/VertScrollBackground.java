package kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects;

import android.graphics.Canvas;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.Sprite;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;


public class VertScrollBackground extends Sprite implements IGameObject {
    private float y;
    private float distanceY = 0f;
    private final float height;


    public VertScrollBackground(int bitmapResId) {
        super(bitmapResId);
        height = bitmap.getHeight() * Metrics.width / bitmap.getWidth();
        setPosition(Metrics.width / 2, Metrics.height / 2);
        setSize(Metrics.width, height);
        distanceY = height;
    }


    public void moveY(float dy) {
        this.y += dy;
    }

    public void addDistanceY(float dy) {
        distanceY += dy;
    }


    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        //super.draw(canvas);
        float curr = y % distanceY;
        if(curr > 0) curr -= distanceY;
        while(curr < Metrics.height) {
            dstRect.set(0, curr, Metrics.width, curr + height);
            canvas.drawBitmap(bitmap, null, dstRect, null);
            curr += distanceY;
        }
    }
}
