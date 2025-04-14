package kr.ac.tukorea.ge.and.jirung_e.doodlejump;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;


public class Player implements IGameObject {
    private final RectF dstRect = new RectF();
    private static final float WIDTH = Metrics.width / 9.0f * 1.8f;
    private static final float WIDTH_HALF = WIDTH / 2;
    private final float HEIGHT;
    private final float HEIGHT_HALF;
    public float x, y;
    public float dx, dy;
    public BoxCollider collider;
    private static final float GRAVITY = 9.8f * WIDTH * 1.4f;
    private static final float JUMP_SPEED = -GRAVITY * 0.6f;
    private final Bitmap bitmap;


    public Player() {
        bitmap = BitmapPool.get(R.mipmap.character_left);
        HEIGHT = WIDTH * ((float) bitmap.getHeight() / bitmap.getWidth());
        HEIGHT_HALF = HEIGHT / 2;

        x = Metrics.width / 2;
        y = Metrics.height;
        dx = 0;
        dy = JUMP_SPEED;
        collider = new BoxCollider(WIDTH_HALF, HEIGHT);

        updateCollider();
        updateRect();
    }


    @Override
    public void update() {
        x += dx * GameView.frameTime;
        y += dy * GameView.frameTime;
        dy += GRAVITY * GameView.frameTime;
        updateCollider();
        updateRect();
    }

    void jump() {
        dy = JUMP_SPEED;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, dstRect, null);
        collider.draw(canvas);
    }

    private void updateRect() {
        dstRect.set(x - WIDTH_HALF, y - HEIGHT,
                x + WIDTH_HALF, y);
    }

    private void updateCollider() {
        collider.setPosition(x, y - HEIGHT_HALF);
    }
}
