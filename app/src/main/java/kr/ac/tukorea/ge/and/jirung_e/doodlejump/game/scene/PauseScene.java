package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene;

import android.graphics.Canvas;
import android.graphics.Rect;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.Button;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.Sprite;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.scene.Scene;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;

public class PauseScene extends Scene {
    private static final Sprite background = new Sprite(R.mipmap.pause_cover);
    private static final Sprite background_sub = new Sprite(R.mipmap.pause_cover);
    private final Button resumeButton;

    public PauseScene() {
        float width = Metrics.width;

        float bg_height = width * (float)background.getBitmap().getHeight() / background.getBitmap().getWidth();
        background.setSize(width, bg_height);
        background.setPosition(width / 2f, bg_height / 2f);

        Rect bgSubSrcRect = new Rect(0, 600, background_sub.getBitmap().getWidth(), background_sub.getBitmap().getHeight());
        background_sub.setSrcRect(bgSubSrcRect);
        float bg_sub_height = width * (float)bgSubSrcRect.height() / bgSubSrcRect.width();
        background_sub.setSize(width, bg_sub_height);
        background_sub.setPosition(width / 2f, bg_height + bg_sub_height / 2f);

        Sprite resumeButtonSprite = new Sprite(R.mipmap.resume_button);
        float buttonWidth = width * 0.36f;
        float buttonHeight = buttonWidth * (float)resumeButtonSprite.getBitmap().getHeight() / resumeButtonSprite.getBitmap().getWidth();
        resumeButtonSprite.setSize(buttonWidth, buttonHeight);
        resumeButton = new Button(
                resumeButtonSprite,
                resumeButtonSprite,
                new Button.OnTouchListener() {
                    @Override
                    public boolean onTouch(boolean pressed) {
                        if(!pressed) {
                            resume();
                        }
                        return true;
                    }
                }
        );
        resumeButton.setPosition(width * 0.7f, Metrics.height * 0.8f);

        controllers.add(resumeButton);
    }

    // Overridables


    @Override
    public void draw(Canvas canvas) {
        background.draw(canvas);
        background_sub.draw(canvas);
        resumeButton.draw(canvas);
        super.draw(canvas);
    }


    @Override
    public boolean onBackPressed() {
        return super.onBackPressed(); // pop this scene
    }

    @Override
    public boolean isTransparent() {
        return true;
    }


    private void resume() {
        GameView.view.popScene();
    }
}
