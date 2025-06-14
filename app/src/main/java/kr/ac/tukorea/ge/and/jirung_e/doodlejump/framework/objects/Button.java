package kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects;

import android.graphics.Canvas;
import android.view.MotionEvent;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.Sprite;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;

public class Button implements ITouchable {
    private static final String TAG = Button.class.getSimpleName();
    public interface OnTouchListener {
        boolean onTouch(boolean pressed);
    }
    protected OnTouchListener listener;
    protected Sprite defaultSprite;
    protected Sprite pressedSprite;


    public Button(Sprite defaultSprite, Sprite pressedSprite, OnTouchListener listener) {
        this.defaultSprite = defaultSprite;
        this.pressedSprite = pressedSprite;
        this.listener = listener;
    }


    public void draw(Canvas canvas) {
        Sprite sprite = captures ? pressedSprite : defaultSprite;
        sprite.draw(canvas);
    }


    public void setPosition(float x, float y) {
        defaultSprite.setPosition(x, y);
        pressedSprite.setPosition(x, y);
    }

    public void move(float dx, float dy) {
        defaultSprite.move(dx, dy);
        pressedSprite.move(dx, dy);
    }


    protected boolean captures;
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getAction();
        //Log.d(TAG, "onTouch:" + this + " action=" + action);
        Sprite sprite = captures ? pressedSprite : defaultSprite;
        if (action == MotionEvent.ACTION_DOWN) {
            float[] pts = Metrics.fromScreen(e.getX(), e.getY());
            float x = pts[0], y = pts[1];
            if (!sprite.getDstRect().contains(x, y)) {
                return false;
            }
            captures = true;
            return listener.onTouch(true);
        } else if (action == MotionEvent.ACTION_UP) {
            captures = false;
            return listener.onTouch(false);
        }
        return captures;
    }
}
