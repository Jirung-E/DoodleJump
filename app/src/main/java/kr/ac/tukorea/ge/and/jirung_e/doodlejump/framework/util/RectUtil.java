package kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.util;

import android.graphics.RectF;

public class RectUtil {
    public static RectF newRectF(float x, float y, float radius) {
        return new RectF(x - radius, y - radius, x + radius, y + radius);
    }
    public static void setRect(RectF rect, float x, float y, float radius) {
        rect.set(x - radius, y - radius, x + radius, y + radius);
    }
    public static RectF newRectF(float x, float y, float width, float height) {
        float half_width = width / 2.0f;
        float half_height = height / 2.0f;
        return new RectF(x - half_width, y - half_height, x + half_width, y + half_height);
    }
    public static void setRect(RectF rect, float x, float y, float width, float height) {
        float half_width = width / 2.0f;
        float half_height = height / 2.0f;
        rect.set(x - half_width, y - half_height, x + half_width, y + half_height);
    }

    public static void setRect(RectF rect, float width, float height) {
        float x = rect.centerX();
        float y = rect.centerY();
        float half_width = width / 2.0f;
        float half_height = height / 2.0f;
        rect.set(x - half_width, y - half_height, x + half_width, y + half_height);
    }
}
