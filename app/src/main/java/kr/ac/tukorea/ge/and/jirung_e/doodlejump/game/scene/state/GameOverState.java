package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.state;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.Button;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.BitmapPool;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.Sprite;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.objects.Score;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.InGameScene;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.Layer;


public class GameOverState implements IGameState {
    private final InGameScene scene;


    public GameOverState(InGameScene scene) {
        this.scene = scene;
    }


    @Override
    public void enter() {
        GameOverObject gameOverObject = new GameOverObject(scene);

        scene.add(Layer.ui, gameOverObject);
    }

    @Override
    public void update() {
        scene.superUpdate();
    }

    @Override
    public void exit() {

    }

    @Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        // 처리하지 않음(버튼을 이미 controller에 등록함)
        return false;
    }
}


class GameOverObject implements IGameObject {
    public float y;

    private final InGameScene scene;

    public final Sprite gameOverTextSprite;
    public final Sprite scoreSprite;
    public final Sprite bottomSprite;

    public final Button playAgainButton;
    private final Button menuButton;
    private final Score score;


    public GameOverObject(InGameScene scene) {
        this.scene = scene;

        y = Metrics.height * 1.5f;

        Bitmap bitmap = BitmapPool.get(R.mipmap.start_end_tiles);

        gameOverTextSprite = new Sprite(bitmap);
        scoreSprite = new Sprite(bitmap);
        bottomSprite = new Sprite(bitmap);

        Rect gameOverTextSrcRect = new Rect(0, 200, 434, 360);
        float gameOverTextWidth = Metrics.width * 0.7f;
        float gameOverTextHeight = gameOverTextWidth * (float)gameOverTextSrcRect.height() / gameOverTextSrcRect.width();
        gameOverTextSprite.setSrcRect(gameOverTextSrcRect);
        gameOverTextSprite.setSize(gameOverTextWidth, gameOverTextHeight);
        gameOverTextSprite.setPosition(Metrics.width / 2, Metrics.height * 0.25f);
        gameOverTextSprite.move(0, y);

        Rect scoreSrcRect = new Rect(790, 330, 1024, 380);
        float scoreTextWidth = Metrics.width * 0.4f;
        float scoreTextHeight = scoreTextWidth * (float)scoreSrcRect.height() / scoreSrcRect.width();
        scoreSprite.setSrcRect(scoreSrcRect);
        scoreSprite.setSize(scoreTextWidth, scoreTextHeight);
        float scoreTextX = Metrics.width * 0.45f;
        float scoreTextY = Metrics.height * 0.4f;
        scoreSprite.setPosition(scoreTextX, scoreTextY);
        scoreSprite.move(0, y);

        Rect bottomSrcRect = new Rect(2, 370, 642, 511);
        float bottomHeight = Metrics.width * (float)bottomSrcRect.height() / bottomSrcRect.width();
        bottomSprite.setSrcRect(bottomSrcRect);
        bottomSprite.setSize(Metrics.width, bottomHeight);
        bottomSprite.setPosition(Metrics.width / 2, Metrics.height - bottomHeight / 2);
        bottomSprite.move(0, y);

        Sprite playAgainSprite = new Sprite(bitmap);
        Sprite playAgainPressedSprite = new Sprite(R.mipmap.play_again_pressed);
        Rect playAgainSrcRect = new Rect(0, 0, 224, 82);
        playAgainSrcRect.offset(230, 98);
        playAgainSprite.setSrcRect(playAgainSrcRect);
        float playAgainButtonWidth = Metrics.width * 0.36f;
        float playAgainButtonHeight = playAgainButtonWidth * (float)playAgainSrcRect.height() / playAgainSrcRect.width();
        playAgainSprite.setSize(playAgainButtonWidth, playAgainButtonHeight);
        playAgainPressedSprite.setSize(playAgainButtonWidth, playAgainButtonHeight);
        playAgainButton = new Button(
                playAgainSprite,
                playAgainPressedSprite,
                new Button.OnTouchListener() {
                    @Override
                    public boolean onTouch(boolean pressed) {
                        if(!pressed) {
                            scene.restart();
                        }
                        return true;
                    }
                }
        );
        playAgainButton.setPosition(Metrics.width * 0.6f, Metrics.height * 0.6f);
        playAgainButton.move(0, y);
        scene.addController(playAgainButton);

        Sprite menuSprite = new Sprite(R.mipmap.menu);
        Sprite menuPressedSprite = new Sprite(R.mipmap.menu_pressed);
        float menuButtonWidth = Metrics.width * 0.36f;
        float menuButtonHeight = menuButtonWidth * (float)menuSprite.getBitmap().getHeight() / menuSprite.getBitmap().getWidth();
        menuSprite.setSize(menuButtonWidth, menuButtonHeight);
        menuPressedSprite.setSize(menuButtonWidth, menuButtonHeight);
        menuButton = new Button(
                menuSprite,
                menuPressedSprite,
                new Button.OnTouchListener() {
                    @Override
                    public boolean onTouch(boolean pressed) {
                        if(!pressed) {
                            GameView.view.popScene();
                        }
                        return true;
                    }
                }
        );
        menuButton.setPosition(Metrics.width * 0.7f, Metrics.height * 0.7f);
        menuButton.move(0, y);
        scene.addController(menuButton);

        score = new Score(scoreTextX + scoreTextWidth / 2 + 20, scoreTextY + scoreTextHeight / 4, scoreTextHeight * 0.6f);
        score.setScore((int)scene.score.getScore());
        scene.add(Layer.ui, score);
        score.move(0, y);
    }


    @Override
    public void update() {
        if(y > 0) {
            float dy = scene.player.dy * GameView.frameTime;
            y -= dy;
            if(y < 0) {
                dy = y + dy;
                y = 0;
            }

            scene.scrollDown(-dy);

            gameOverTextSprite.move(0, -dy);
            scoreSprite.move(0, -dy);
            bottomSprite.move(0, -dy);
            playAgainButton.move(0, -dy);
            menuButton.move(0, -dy);
            score.move(0, -dy);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        gameOverTextSprite.draw(canvas);
        scoreSprite.draw(canvas);
        bottomSprite.draw(canvas);

        playAgainButton.draw(canvas);
        menuButton.draw(canvas);
    }
}