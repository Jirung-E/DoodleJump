package kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.util.RectUtil;

public class Sprite {
    private static final String TAG = Sprite.class.getSimpleName();
    protected Bitmap bitmap;
    protected Rect srcRect = null;
    protected final RectF dstRect = new RectF();
    protected float offset_x = 0;
    protected float offset_y = 0;
    protected float width, height, radius;


    ///////////////////////////////////////// Constructors /////////////////////////////////////////
    public Sprite(int mipmapId) {
        if (mipmapId != 0) {
            bitmap = BitmapPool.get(mipmapId);
        }
        Log.v(TAG, "Created " + this.getClass().getSimpleName() + "@" + System.identityHashCode(this));
    }
    public Sprite(Bitmap bitmap) {
        this.bitmap = bitmap;
        Log.v(TAG, "Created " + this.getClass().getSimpleName() + "@" + System.identityHashCode(this));
    }


    /////////////////////////////////////////// Getters  ///////////////////////////////////////////
    public final Bitmap getBitmap() {
        return bitmap;
    }

    public final float getWidth() {
        return width;
    }

    public final float getHeight() {
        return height;
    }

    /////////////////////////////////////////// Setters  ///////////////////////////////////////////
    public void setImageResourceId(int mipmapId) {
        bitmap = BitmapPool.get(mipmapId);
    }
    public void setImageResourceId(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setSrcRect(int left, int top, int right, int bottom) {
        if(srcRect == null) {
            srcRect = new Rect(left, top, right, bottom);
        }
        else {
            srcRect.set(left, top, right, bottom);
        }
    }

    public void setSrcRect(Rect srcRect) {
        if(this.srcRect == null) {
            this.srcRect = new Rect(srcRect);
        }
        else {
            this.srcRect.set(srcRect);
        }
    }

    public void setOffset(float off_x, float off_y) {
        offset_x = off_x;
        offset_y = off_y;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        this.radius = Math.min(width, height) / 2.0f;
        RectUtil.setRect(dstRect, this.width, this.height);
    }

    public void setSize(float radius) {
        this.width = this.height = 2 * radius;
        this.radius = radius;
        RectUtil.setRect(dstRect, this.width, this.height);
    }

    public void setPosition(float x, float y) {
        x += offset_x;
        y += offset_y;
        RectUtil.setRect(dstRect, x, y, width, height);
    }


    //////////////////////////////////////////// Methods ///////////////////////////////////////////
    public void move(float x, float y) {
        dstRect.offset(x, y);
    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }
}
