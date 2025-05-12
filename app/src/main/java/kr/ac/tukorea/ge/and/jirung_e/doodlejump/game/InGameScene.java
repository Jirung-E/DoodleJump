package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.scene.Scene;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.physics.CcdResult;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.item.Item;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile.BrokenTile;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile.NormalTile;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.item.Spring;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile.Tile;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile.TileLoader;

public class InGameScene extends Scene {
    private static final String TAG = InGameScene.class.getSimpleName();
    private Player player;
    private float score;
    private TileLoader tileLoader;
    private final float MIN_HEIGHT;
    public static final float GRAVITY = 9.8f * 256f;


    ///////////////////////////////////////// Constructors /////////////////////////////////////////
    public InGameScene() {
        Metrics.setGameSize(900, 1600);
        MIN_HEIGHT = Metrics.height / 2.0f;

        initLayers(InGameLayer.COUNT);

        tileLoader = new TileLoader(this);
        add(tileLoader);

        player = new Player();

        Tile tile = new NormalTile(Metrics.width / 2, Metrics.height * 0.95f);
        add(tile);

        add(player);
    }


    //////////////////////////////////////////// Methods ///////////////////////////////////////////
    @Override
    public void update() {
        float prev_y = player.y;

        float nearest_t = Float.POSITIVE_INFINITY;
        IGameObject collidee = null;
        for(IGameObject obj : objectsAt(InGameLayer.tile)) {
            Tile tile = (Tile)obj;
            // 아래로 내려가는 중에 충돌하는 경우
            if(player.dy > 0) {
                CcdResult result = player.collider.ccd(tile.collider, player.dx * GameView.frameTime, player.dy * GameView.frameTime);
                if(result.isCollide) {
                    if (-0.1f <= result.t && result.t < nearest_t) {
                        if (result.ny < 0) {
                            nearest_t = result.t;
                            collidee = tile;
                        }
                    }
                }
            }
        }
        for(IGameObject obj : objectsAt(InGameLayer.item)) {
            Item item = (Item)obj;
            CcdResult result = player.collider.ccd(item.collider, player.dx * GameView.frameTime, player.dy * GameView.frameTime);
            if(player.dy > 0) {
                if(result.isCollide) {
                    if(item instanceof Spring) {
                        if (-0.1f <= result.t && result.t < nearest_t) {
                            if (result.ny < 0) {
                                nearest_t = result.t;
                                collidee = item;
                            }
                        }
                    }
                    else {
                        // 아이템 충돌 처리
                    }
                }
            }
        }
        if(nearest_t < Float.POSITIVE_INFINITY) {
            if(collidee instanceof Tile) {
                // 여러개 겹쳐있는 경우 하나만 처리하게 되지만, 겹쳐있게 만들지 않을거임
                if(collidee instanceof BrokenTile) {
                    ((BrokenTile)collidee).trigger();
                }
                else {
                    player.y = player.y + player.dy * GameView.frameTime * nearest_t;
                    player.jump();
                }
            }
            else if(collidee instanceof Spring) {
                player.y = player.y + player.dy * GameView.frameTime * nearest_t;
                player.jump(2);
                ((Spring)collidee).trigger();
            }
//            else if(collidee instanceof Item) {
//                ((Item)collidee).onCollision(player);
//                remove(collidee);
//            }
        }

        super.update();

        if(player.x < 0) {
            player.x = Metrics.width;
        }
        else if(player.x > Metrics.width) {
            player.x = 0;
        }

        // 일정 높이 이상으로 올라가면 타일 등의 오브젝트를 아래로 이동시킴 + 스코어 증가
        if(player.y < MIN_HEIGHT) {
            float diff = MIN_HEIGHT - player.y;

            player.y = MIN_HEIGHT;
            score += diff / 10f;
            tileLoader.setDifficulty(score / 5000.0f);

            tileLoader.y += diff;

            ArrayList<IGameObject> tiles = objectsAt(InGameLayer.tile);
            for(int i=tiles.size()-1; i>=0; --i) {
                Tile tile = (Tile)(tiles.get(i));
                tile.y += diff;
                float tile_y = tile.collider.getTop();
                if(tile_y > Metrics.height) {
                    tile.collider.isActive = false;
                }
                if(tile.item != null) {
                    tile_y = tile.item.collider.getTop();
                }
                if(tile_y > Metrics.height) {
                    if(tile.item != null) {
                        remove(tile.item);
                    }
                    remove(tile);
                }
            }
        }
    }


    private Paint scorePaint;
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (scorePaint == null) {
            scorePaint = new Paint();
            scorePaint.setColor(Color.BLUE);
            scorePaint.setTypeface(Typeface.MONOSPACE);
            scorePaint.setTextSize(32f);
        }

        if(GameView.drawsDebugStuffs) {
            canvas.drawText("SCORE: " + score, 0f, 40f, scorePaint);
            canvas.drawText("difficulty: " + tileLoader.getDifficulty(), 0f, 80f, scorePaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float[] pts = Metrics.fromScreen(event.getX(), event.getY());
                if(pts[0] >= Metrics.width / 2) {
                    player.setTargetMoveDirection(1);
                }
                else {
                    player.setTargetMoveDirection(-1);
                }
                return true;
            case MotionEvent.ACTION_UP:
                player.setTargetMoveDirection(0);
                return true;
        }
        return false;
    }
}
