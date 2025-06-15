package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.objects;

import android.graphics.Canvas;

import java.util.Random;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.ILayerProvider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.item.Jetpack;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.item.Propeller;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.item.Spring;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.monster.Monster;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.monster.Monster1;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.Layer;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.InGameScene;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile.BrokenTile;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile.MovingTile;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile.NormalTile;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile.Tile;

public class MapLoader implements IGameObject, ILayerProvider<Layer> {
    private static final String TAG = MapLoader.class.getSimpleName();

    private final InGameScene scene;
    private final Random random = new Random();
    public float y;
    private static final float HOME_Y = -100f;    // 적당한 높이값으로 설정
    private final int Y_RANGE;
    /// 0(EASY)~1(HARD)
    private float difficulty;
    private static final int MIN_DISTANCE = 100;    // 적당한 최소거리 설정
    private static final int BROKEN_TILE_INTERVAL = 400;
    private float brokenTileGenerateCounter = 0;


    public MapLoader(InGameScene mainScene) {
        scene = mainScene;
        y = Metrics.height * 0.8f;
        Y_RANGE = (int)(Metrics.height * 0.18f);
        difficulty = 0f;
    }


    public void update() {
        // 일정 간격마다 랜덤한 x값을 가지고 타일을 생성
        while(y > HOME_Y) {
            genTile();

            if(brokenTileGenerateCounter > BROKEN_TILE_INTERVAL) {
                // 다음에 생성되는 밟을 수 있는 Tile의 위치
                float between = random.nextInt(Y_RANGE - MIN_DISTANCE) * difficulty;
                float amount = between + MIN_DISTANCE * 2;
                y -= amount;
                brokenTileGenerateCounter = 0;

                // 사이에 BrokenTile 또는 Monster 생성
                float minY = y + MIN_DISTANCE;
                float maxY = y + amount - MIN_DISTANCE;

                // BrokenTile 생성
                if(random.nextInt(2) == 0) {
                    int remained = (int)between;
                    amount = random.nextInt(remained + 1);
                    remained -= (int)Math.ceil(amount);
                    float brokenTileY = maxY - amount;
                    do {
                        // 생성
                        Tile brokenTile = scene.getObject(BrokenTile.class);
                        brokenTile.x = random.nextInt(Tile.X_RANGE) + Tile.START_X;
                        brokenTile.y = brokenTileY;
                        brokenTile.update();
                        scene.add(brokenTile);

                        amount = random.nextInt(Y_RANGE) * difficulty + MIN_DISTANCE;
                        brokenTileY -= amount;
                        remained -= (int)Math.ceil(amount);
                    } while(remained >= 0);
                }
                // Monster 생성
                else {
                    if(random.nextInt(2) == 0) {
                        Monster monster = scene.getObject(Monster1.class);
                        monster.x = random.nextInt(Tile.X_RANGE) + Tile.START_X;
                        monster.y = (minY + maxY) / 2;
                        monster.update();
                        scene.add(monster);
                    }
                }
            }
            else {
                // 다음에 생성되는 Tile의 위치
                float amount = random.nextInt(Y_RANGE) * difficulty + MIN_DISTANCE;
                y -= amount;
                brokenTileGenerateCounter += amount;
            }
        }
    }

    public void genTile() {
        Tile tile;
        // 난이도가 0.5보다 작으면 MovingTile을 생성하지 않음
        if(difficulty < 0.5 || random.nextInt(20 - (int)(36 * (difficulty - 0.5))) != 0) {
            tile = scene.getObject(NormalTile.class);
        }
        else {
            tile = scene.getObject(MovingTile.class);
        }
        tile.x = random.nextInt(Tile.X_RANGE) + Tile.START_X;
        tile.y = y;
        tile.update();
        scene.add(tile);

        // 5% 확률로 아이템 생성
        if(random.nextInt(20) == 0) {
            // n% 확률로 스프링 생성
            if(random.nextInt(2) == 0) {
                Spring spring = scene.getObject(Spring.class);
                spring.setParent(tile);
                spring.update();
                scene.add(spring);
            }
            else {
//                if(difficulty > 0.2) {
                    // 기타 아이템 생성
                    if(random.nextInt(2) == 0) {
                        Jetpack jetpack = scene.getObject(Jetpack.class);
                        jetpack.setParent(tile);
                        jetpack.update();
                        scene.add(jetpack);
                    }
                    else {
                        Propeller propeller = scene.getObject(Propeller.class);
                        propeller.setParent(tile);
                        propeller.update();
                        scene.add(propeller);
                    }
//                }
            }
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
    public Layer getLayer() {
        return Layer.controller;
    }
}
