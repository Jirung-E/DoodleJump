package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene;

import android.graphics.Canvas;
import android.graphics.Rect;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.Button;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.physics.CcdResult;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.Sprite;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.scene.Scene;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player.Player;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile.NormalTile;

public class LobbyScene extends Scene {
    private Player player;
    private NormalTile tile;

    private static final Sprite background = new Sprite(R.mipmap.background);
    private static final Sprite background_sub = new Sprite(R.mipmap.background);
    private static final Sprite bottom = new Sprite(R.mipmap.start_end_tiles);
    private static final Rect bottomSrcRect = new Rect(2, 372, 642, 511);
    private static final Sprite title = new Sprite(R.mipmap.title);
    private final Button playButton;


    public LobbyScene() {
        float width = Metrics.width;

        float bg_height = width * (float)background.getBitmap().getHeight() / background.getBitmap().getWidth();
        background.setSize(width, bg_height);
        background.setPosition(width / 2f, bg_height / 2f);

        background_sub.setSize(width, bg_height);
        background_sub.setPosition(width / 2f, Metrics.height - bg_height / 2f - 6);

        float bottom_height = width * (float)bottomSrcRect.height() / bottomSrcRect.width();
        bottom.setSize(width, bottom_height);
        bottom.setPosition(width / 2f, Metrics.height - bottom_height / 2f);
        bottom.setSrcRect(bottomSrcRect);

        float title_width = width * 0.6f;
        float title_height = title_width * (float)title.getBitmap().getHeight() / title.getBitmap().getWidth();
        float title_x = title_width * 0.57f;
        title.setSize(title_width, title_height);
        title.setPosition(title_x, Metrics.height * 0.2f + title_height / 2f);

        Sprite playButtonSprite = new Sprite(R.mipmap.play_button);
        Sprite playButtonPressedSprite = new Sprite(R.mipmap.play_button_pressed);
        float playButtonWidth = width * 0.36f;
        float playButtonHeight = playButtonWidth * (float)playButtonSprite.getBitmap().getHeight() / playButtonSprite.getBitmap().getWidth();
        playButtonSprite.setSize(playButtonWidth, playButtonHeight);
        playButtonPressedSprite.setSize(playButtonWidth, playButtonHeight);
        playButton = new Button(
                playButtonSprite,
                playButtonPressedSprite,
                new Button.OnTouchListener() {
                    @Override
                    public boolean onTouch(boolean pressed) {
                        if(!pressed) {
                            startGame();
                        }
                        return true;
                    }
                }
        );
        playButton.setPosition(width * 0.36f, Metrics.height * 0.2f + title_height * 2f);
        controllers.add(playButton);

        initLayers(Layer.COUNT);

        float px = Metrics.width / 5f;

        player = new Player();
        player.x = px;
        player.y = Metrics.height * 1.5f;
        player.jump(1.85f);
        add(player);

        tile = new NormalTile(px, Metrics.height * 0.8f);
        add(tile);
    }


    @Override
    public void update() {
        super.update();

        float dx = player.dx * GameView.frameTime;
        float dy = player.dy * GameView.frameTime;
        CcdResult result = player.collider.ccd(tile.collider, dx, dy);
        if(result.isCollide && player.dy > 0) {     // 더 자세하게는 검사 안함
            player.y = tile.collider.getTop();
            player.jump();
        }
    }


    @Override
    public void draw(Canvas canvas) {
        background_sub.draw(canvas);
        background.draw(canvas);
        bottom.draw(canvas);
        title.draw(canvas);
        playButton.draw(canvas);
        super.draw(canvas);
    }


    private void startGame() {
        GameView.view.pushScene(new InGameScene());
    }
}
