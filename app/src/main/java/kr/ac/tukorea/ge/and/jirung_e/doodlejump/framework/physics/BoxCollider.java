package kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.physics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.util.RectUtil;


public class BoxCollider {
    private RectF rect;
    private static final Paint paint = new Paint();
    public boolean isActive = true;


    public BoxCollider(float width, float height) {
        this(0, 0, width, height);
    }

    public BoxCollider(float x, float y, float width, float height) {
        rect = new RectF(x - width / 2, y - height / 2,
                x + width / 2, y + height / 2);
        initPaint();
    }

    public BoxCollider(RectF rect) {
        this.rect = rect;
        initPaint();
    }


    private void initPaint() {
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
    }


    public void setPosition(float x, float y) {
        float width = rect.width();
        float height = rect.height();
        rect.left = x - width / 2;
        rect.top = y - height / 2;
        rect.right = x + width / 2;
        rect.bottom = y + height / 2;
    }

    public void setSize(float width, float height) {
        RectUtil.setRect(rect, width, height);
    }

    public boolean isCollide(BoxCollider other) {
        if(!isActive || !other.isActive) {
            return false;
        }
        return RectF.intersects(this.rect, other.rect);
    }

    /// 정확하지 않음
    public CcdResult ccd(BoxCollider other, float dx, float dy) {
        CcdResult result = new CcdResult();

        if(!isActive || !other.isActive) {
            return result;
        }

        if(isCollide(other)) {
            result.isCollide = true;
            result.t = 0;
            return result;
        }

        RectF swept = new RectF(rect);
        if(dx > 0) {
            swept.right += dx;
        }
        else {
            swept.left += dx;
        }
        if(dy > 0) {
            swept.bottom += dy;
        }
        else {
            swept.top += dy;
        }

        if(!RectF.intersects(swept, other.rect)) {
            return result;
        }

        // 충돌하는 경우
        float distX = 0;
        float distY = 0;
        if(dx > 0) {
            distX = other.rect.left - rect.right;
        }
        else {
            distX = other.rect.right - rect.left;
        }
        if(dy > 0) {
            distY = other.rect.top - rect.bottom;
        }
        else {
            distY = other.rect.bottom - rect.top;
        }

        float tx = Float.POSITIVE_INFINITY;
        float ty = Float.POSITIVE_INFINITY;

        if (dx != 0) {
            tx = distX / dx;
        }
        if (dy != 0) {
            ty = distY / dy;
        }

        boolean validX = (0 <= tx && tx <= 1) || dx == 0;
        boolean validY = (0 <= ty && ty <= 1) || dy == 0;
//
//        if(!validX || !validY) {
//            return result;
//        }

        if(tx == Float.POSITIVE_INFINITY) {
            tx = 0;
        }
        if(ty == Float.POSITIVE_INFINITY) {
            ty = 0;
        }

        result.isCollide = true;
        if(tx > ty) {
            result.t = tx;
            result.nx = distX > 0 ? -1 : 1;
            result.ny = 0;
        }
        else {
            result.t = ty;
            result.nx = 0;
            result.ny = distY > 0 ? -1 : 1;
        }

        return result;
    }

    public float getTop() {
        return rect.top;
    }

    public float getBottom() {
        return rect.bottom;
    }

    public void draw(Canvas canvas) {
        if(isActive) {
            paint.setColor(Color.GREEN);
        }
        else {
            paint.setColor(Color.RED);
        }
        canvas.drawRect(rect, paint);
    }
}
