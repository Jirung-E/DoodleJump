package kr.ac.tukorea.ge.and.jirung_e.doodlejump;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;

import androidx.annotation.NonNull;

public class GameView extends View implements Choreographer.FrameCallback {
    public static final float SCREEN_WIDTH = 9.0f;
    public static final float SCREEN_HEIGHT = 16.0f;
    public static float frameTime;

    private Player player = new Player();
    private Tile tile = new Tile(SCREEN_WIDTH / 2, 12);

    private final Matrix transformMatrix = new Matrix();
    private final Matrix invertedMatrix = new Matrix();
    private static long previousNanos;

    private String TAG = GameView.class.getSimpleName();


    public GameView(Context context) {      // AttributeSet - 미리보기를 위해 적어두기
        super(context);

        Resources res = getResources();

        Bitmap playerBitmap = BitmapFactory.decodeResource(res, R.mipmap.character_left);
        Player.setBitmap(playerBitmap);

        Bitmap tileBitmap = BitmapFactory.decodeResource(res, R.mipmap.tiles);
        Tile.setBitmap(tileBitmap);

        scheduleUpdate();
    }


    private void update() {
        float prev_y = player.y;

        player.update();

        // 아래로 내려가는 중에 충돌하는 경우
        if(player.dy > 0 && player.collider.isCollide(tile.collider)) {
            float top = tile.collider.getTop();
            // 이전프레임에 타일보다 위에 있던 경우
            if(prev_y < top) {
                player.y = top;
                player.jump();
            }
        }
    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.setMatrix(transformMatrix);

        tile.draw(canvas);
        player.draw(canvas);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float view_ratio = (float)w / (float)h;
        float game_ratio = SCREEN_WIDTH / SCREEN_HEIGHT;

        transformMatrix.reset();
        if (view_ratio > game_ratio) {
            float scale = h / SCREEN_HEIGHT;
            transformMatrix.preTranslate((w - h * game_ratio) / 2, 0);
            transformMatrix.preScale(scale, scale);
        } else {
            float scale = w / SCREEN_WIDTH;
            transformMatrix.preTranslate(0, (h - w / game_ratio) / 2);
            transformMatrix.preScale(scale, scale);
        }
        transformMatrix.invert(invertedMatrix);
    }

    @Override
    public void doFrame(long nanos) {
        if (previousNanos != 0) {
            frameTime = (nanos - previousNanos) / 1_000_000_000f;
            update();
            invalidate();
        }
        previousNanos = nanos;
        if (isShown()) {
            scheduleUpdate();
        }
    }

    private void scheduleUpdate() {
        Choreographer.getInstance().postFrameCallback(this);
    }
}