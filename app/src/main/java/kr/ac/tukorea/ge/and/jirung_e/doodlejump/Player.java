package kr.ac.tukorea.ge.and.jirung_e.doodlejump;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

public class Player {
    private final RectF dstRect = new RectF();
    private static final float WIDTH_HALF = 1.0f;
    private static final float HEIGHT_HALF = 1.0f;
    private float x, y;
    private float dx, dy;
    private static final float GRAVITY = 9.8f;
    private static final float JUMP_SPEED = -10.0f;

    public Player() {
        x = GameView.SCREEN_WIDTH / 2;
        y = GameView.SCREEN_HEIGHT / 2;
        dx = 0;
        dy = JUMP_SPEED;

        updateRect();
    }

    private static Bitmap bitmap;
    public static void setBitmap(Bitmap bitmap) {
        Player.bitmap = bitmap;
    }

    public void update() {
        dy += GRAVITY * GameView.frameTime;
        x += dx * GameView.frameTime;
        y += dy * GameView.frameTime;
        updateRect();

        if(y > GameView.SCREEN_HEIGHT) {
            y = GameView.SCREEN_HEIGHT;
            dy = JUMP_SPEED;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, dstRect, null);
    }

    private void updateRect() {
        dstRect.set(x - WIDTH_HALF, y - HEIGHT_HALF,
                x + WIDTH_HALF, y + HEIGHT_HALF);
    }
}
