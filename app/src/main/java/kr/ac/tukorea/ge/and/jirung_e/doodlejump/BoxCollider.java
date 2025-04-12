package kr.ac.tukorea.ge.and.jirung_e.doodlejump;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class BoxCollider {
    private RectF rect;

    public BoxCollider(float width, float height) {
        rect = new RectF(-width / 2, -height / 2,
                width / 2, height / 2);
    }

    public BoxCollider(float x, float y, float width, float height) {
        rect = new RectF(x - width / 2, y - height / 2,
                x + width / 2, y + height / 2);
    }

    public void setPosition(float x, float y) {
        float width = rect.width();
        float height = rect.height();
        rect.left = x - width / 2;
        rect.top = y - height / 2;
        rect.right = x + width / 2;
        rect.bottom = y + height / 2;
    }

    public boolean isCollide(BoxCollider other) {
        return RectF.intersects(this.rect, other.rect);
    }

    public float getTop() {
        return rect.top;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rect, paint);
    }
}
