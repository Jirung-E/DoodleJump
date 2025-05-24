package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player.equipment;

import android.graphics.Canvas;
import android.graphics.Rect;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.Sprite;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player.Action;

public class Jetpack extends Equipment {
    private final Sprite sprite;
    private static final Rect[] srcRect = {
        new Rect(0, 0, 64, 128),
        new Rect(64, 0, 128, 128),
        new Rect(128, 0, 192, 128),
        new Rect(192, 0, 256, 128),
        new Rect(0, 128, 64, 256),
        new Rect(64, 128, 128, 256),
        new Rect(128, 128, 192, 256),
        new Rect(192, 128, 256, 256),
        new Rect(0, 256, 64, 384),
        new Rect(64, 256, 128, 384),
    };
    enum Phase {
        BOOST_START,
        BOOSTING,
        BOOST_END,
    }
    private static final int[][] animationFrames = {
        {9, 0, 1, 2},       // BOOST_START
        {3, 4, 5},          // BOOSTING
        {6, 7, 8, 9}        // BOOST_END
    };
    private static final float animationSpeed = 20f; // frames per second
    private float animationTime = 0.0f;

    private float x, y;
    private Action action = Action.LEFT; // 기본값은 왼쪽


    public Jetpack() {
        sprite = new Sprite(R.mipmap.jetpack);
        sprite.setOffset(60f, -20.0f);
        float width = Metrics.width / 9.0f;
        float height = width * ((float) srcRect[0].height() / srcRect[0].width());
        sprite.setSize(width, height); // 크기 조정
    }


    @Override
    public void update(float x, float y, Action action, float deltaTime) {
        this.x = x;
        this.y = y;
        this.action = action;

        sprite.setPosition(x, y);

        animationTime += deltaTime;

        int frameCount = animationFrames[Phase.BOOSTING.ordinal()].length;
        int frameIndex = Math.round(animationTime * animationSpeed) % frameCount;
        int srcIndex = animationFrames[Phase.BOOSTING.ordinal()][frameIndex];
        sprite.setSrcRect(srcRect[srcIndex]);
    }


    @Override
    public void draw(Canvas canvas) {
        if((action.ordinal() & 0b01) == Action.LEFT.ordinal()) {
            // 그대로 그리기
            sprite.draw(canvas);
        }
        else {
            // 뒤집어서 그리기
            canvas.save();
            canvas.scale(-1, 1, x, y);
            sprite.draw(canvas);
            canvas.restore();
        }
    }
}
