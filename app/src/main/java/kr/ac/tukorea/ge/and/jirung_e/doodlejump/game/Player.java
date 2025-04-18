package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.BitmapPool;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.physics.BoxCollider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;


public class Player implements IGameObject {
    private static final String TAG = Player.class.getSimpleName();
    private final RectF dstRect = new RectF();
    private static final float WIDTH = Metrics.width / 9.0f * 1.8f;
    private static final float WIDTH_HALF = WIDTH / 2;
    private final float HEIGHT;
    private final float HEIGHT_HALF;
    public float x, y;
    public float dx, dy;
    private float target_dx;

    public BoxCollider collider;
    private static final float GRAVITY = 9.8f * WIDTH * 1.4f;
    private static final float JUMP_SPEED = -GRAVITY * 0.6f;
    private static final float MOVE_SPEED = Metrics.width;
    private static final float ACCELERATION_X = 4 * MOVE_SPEED;
    private final Bitmap bitmap;


    public Player() {
        bitmap = BitmapPool.get(R.mipmap.character_left);
        HEIGHT = WIDTH * ((float) bitmap.getHeight() / bitmap.getWidth());
        HEIGHT_HALF = HEIGHT / 2;

        x = Metrics.width / 2;
        y = Metrics.height;
        dx = 0;
        dy = JUMP_SPEED;
        target_dx = 0;
        collider = new BoxCollider(WIDTH_HALF, HEIGHT);

        updateCollider();
        updateRect();
    }


    @Override
    public void update() {
        float prev_dx_sign = Math.signum(dx);
        dx += Math.signum(target_dx - dx) * ACCELERATION_X * GameView.frameTime;
        if(target_dx == 0 && prev_dx_sign != Math.signum(dx)) {
            dx = 0;
        }

        dx = Math.clamp(dx, -MOVE_SPEED, MOVE_SPEED);
        x += dx * GameView.frameTime;
        y += dy * GameView.frameTime;
        dy += GRAVITY * GameView.frameTime;
        // 최고속력 제한
        if(dy > -JUMP_SPEED) {
            dy = -JUMP_SPEED;
        }

        updateCollider();
        updateRect();
    }

    public void jump() {
        dy = JUMP_SPEED;
    }

    /// 크기와 상관 없이 이동방향 설정
    /// - 양수: 오른쪽
    /// - 음수: 왼쪽
    /// - 0: 정지
    public void setXMoveDirection(int dx) {
        this.dx = Math.signum(dx) * MOVE_SPEED;
    }

    /// 크기와 상관 없이 이동방향 설정
    /// - 양수: 오른쪽
    /// - 음수: 왼쪽
    /// - 0: 정지
    public void setTargetMoveDirection(int dx) {
        this.target_dx = Math.signum(dx) * MOVE_SPEED;
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
