package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile;

import android.graphics.Canvas;

import java.util.Random;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.ILayerProvider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.InGameLayer;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.InGameScene;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.item.Spring;

public class TileLoader implements IGameObject, ILayerProvider<InGameLayer> {
    private static final String TAG = TileLoader.class.getSimpleName();

    private final InGameScene scene;
    private final Random random = new Random();
    public float y;
    private static final float HOME_Y = -100f;    // 적당한 높이값으로 설정
    private final int Y_RANGE;
    /// 0(EASY)~1(HARD)
    private float difficulty;
    private static final int MIN_DISTANCE = 100;    // 적당한 최소거리 설정


    public TileLoader(InGameScene mainScene) {
        scene = mainScene;
        y = Metrics.height * 0.8f;
        Y_RANGE = (int)(Metrics.height * 0.18f);
        difficulty = 0f;
    }


    public void update() {
        // 일정 간격마다 랜덤한 x값을 가지고 타일을 생성
        while(y > HOME_Y) {
            // 랜덤 타일 생성
            Tile tile;
            // 난이도가 0.5보다 작으면 MovingTile을 생성하지 않음
            if(difficulty < 0.5 || random.nextBoolean()) {
                tile = scene.getObject(NormalTile.class);
            } else {
                tile = scene.getObject(MovingTile.class);
            }
            tile.x = random.nextInt(Tile.X_RANGE) + Tile.START_X;
            tile.y = y;
            tile.update();
            scene.add(tile);

            if(random.nextInt(10) == 0) {
                // 10% 확률로 Spring 생성
                Spring spring = scene.getObject(Spring.class);
                spring.setParent(tile);
                spring.update();
                scene.add(spring);
            }

            y -= (int)(random.nextInt(Y_RANGE) * difficulty) + MIN_DISTANCE;
        }
    }

    public float getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(float difficulty) {
        this.difficulty = Math.clamp(difficulty, 0f, 1f);
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public InGameLayer getLayer() {
        return InGameLayer.controller;
    }
}
