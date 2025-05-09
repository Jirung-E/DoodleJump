package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game;

import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.ILayerProvider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;

public class TileLoader implements IGameObject, ILayerProvider<InGameLayer> {
    private static final String TAG = TileLoader.class.getSimpleName();

    private final InGameScene scene;
    private final Random random = new Random();
    public float y;
    private static final float HOME_Y = -Tile.IMG_HEIGHT / 2f;
    private final int PADDING_X;
    private final int START_X, END_X;
    private final int X_RANGE;
    private final int Y_RANGE;
    /// 0(EASY)~1(HARD)
    public float difficulty = 1;
    private static final int MIN_DISTANCE = (int)Tile.IMG_HEIGHT * 2;

    public TileLoader(InGameScene mainScene) {
        scene = mainScene;
        y = Metrics.height * 0.8f;
        PADDING_X = (int)(Metrics.width * 0.01f);   // 1%
        START_X = (int)Tile.IMG_WIDTH + PADDING_X;
        END_X = (int)(Metrics.width - Tile.IMG_WIDTH) - PADDING_X;
        X_RANGE = END_X - START_X;
        Y_RANGE = (int)(Metrics.height * 0.2f);
    }

    public void update() {
        // 일정 간격마다 랜덤한 x값을 가지고 타일을 생성
        while(y > HOME_Y) {
            Tile tile = scene.getObject(Tile.class);
            tile.x = random.nextInt(X_RANGE) + START_X;
            tile.y = y;
            tile.update();
            scene.add(tile);
            y -= (int)(random.nextInt(Y_RANGE) * difficulty) + MIN_DISTANCE;
        }
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public InGameLayer getLayer() {
        return InGameLayer.controller;
    }
}
