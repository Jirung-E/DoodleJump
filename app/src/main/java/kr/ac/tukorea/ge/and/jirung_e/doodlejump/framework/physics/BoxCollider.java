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


    public CcdResult ccd(BoxCollider other, float dx, float dy) {
        CcdResult result = new CcdResult();

        if(!isActive || !other.isActive) {
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

        // 각 축에 대해 충돌시간의 범위를 계산
        float tx_min = Float.NEGATIVE_INFINITY;
        float tx_max = Float.POSITIVE_INFINITY;
        float ty_min = Float.NEGATIVE_INFINITY;
        float ty_max = Float.POSITIVE_INFINITY;

        // dx, dy가 0인 경우에는 해당 축은 무조건 충돌함(swept에서 검사 완료)
        if(dx != 0.0) {
            if(dx > 0.0) {
                tx_min = (other.rect.left - rect.right) / dx;
                tx_max = (other.rect.right - rect.left) / dx;
            }
            else {
                tx_min = (other.rect.right - rect.left) / dx;
                tx_max = (other.rect.left - rect.right) / dx;
            }
        }
        if(dy != 0.0) {
            if(dy > 0.0) {
                ty_min = (other.rect.top - rect.bottom) / dy;
                ty_max = (other.rect.bottom - rect.top) / dy;
            }
            else {
                ty_min = (other.rect.bottom - rect.top) / dy;
                ty_max = (other.rect.top - rect.bottom) / dy;
            }
        }

        // 충돌 시간 범위의 교차점을 계산
        float t_min = Math.max(tx_min, ty_min);
        float t_max = Math.min(tx_max, ty_max);
        if(t_min > t_max || t_min > 1.0 || t_max <= 0.0) {
            return result;
        }

        // 최초 충돌 시간
        result.t = t_min;

        if(t_min == tx_min) {
            result.nx = -Math.signum(dx);
        }
        if(t_min == ty_min) {
            result.ny = -Math.signum(dy);
        }

        result.isCollide = true;

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
