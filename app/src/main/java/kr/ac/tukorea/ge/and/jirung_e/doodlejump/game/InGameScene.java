package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game;

import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.scene.Scene;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.physics.CcdResult;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;

public class InGameScene extends Scene {
    private static final String TAG = InGameScene.class.getSimpleName();
    private Player player;
    private TileLoader tileLoader;
    private final float MIN_HEIGHT;


    ///////////////////////////////////////// Constructors /////////////////////////////////////////
    public InGameScene() {
        Metrics.setGameSize(900, 1600);
        MIN_HEIGHT = Metrics.height / 2.0f;

        initLayers(InGameLayer.COUNT);

        tileLoader = new TileLoader(this);
        add(tileLoader);

        player = new Player();

        Tile tile = new Tile(Metrics.width / 2, Metrics.height * 0.95f);
        add(tile);

//        for(int i=0; i<10; ++i) {
//            Tile tile = new Tile(Metrics.width / 2, Metrics.height * 0.8f - i * 200);
//            add(tile);
//        }

        add(player);
    }


    //////////////////////////////////////////// Methods ///////////////////////////////////////////
    @Override
    public void update() {
        float prev_y = player.y;

        super.update();

        if(player.x < 0) {
            player.x = Metrics.width;
        }
        else if(player.x > Metrics.width) {
            player.x = 0;
        }

        float nearest_t = Float.POSITIVE_INFINITY;
        float top = prev_y;
        for(IGameObject obj : objectsAt(InGameLayer.tile)) {
            Tile tile = (Tile)obj;
            // 아래로 내려가는 중에 충돌하는 경우
            CcdResult result = player.collider.ccd(tile.collider, player.dx * GameView.frameTime, player.dy * GameView.frameTime);
            if(player.dy > 0 && result.isCollide) {
                if(result.t < nearest_t) {
                    if(result.ny > 0) {
                        nearest_t = result.t;
                    }
                    if(result.t == 0) {
                        nearest_t = result.t;
                        top = tile.collider.getTop();
                    }
                }
            }
        }
        if(nearest_t < Float.POSITIVE_INFINITY) {
            if(nearest_t == 0) {
                // 이전프레임에 타일보다 위에 있던 경우
                if (prev_y <= top) {
                    player.y = top;
                    player.jump();
                }
            }
            else {
                player.y = player.y + player.dy * GameView.frameTime * nearest_t;
                player.jump();
            }
        }

        // 일정 높이 이상으로 올라가면 타일 등의 오브젝트를 아래로 이동시킴
        if(player.y < MIN_HEIGHT) {
            float diff = MIN_HEIGHT - player.y;

            player.y = MIN_HEIGHT;

            tileLoader.y += diff;

            ArrayList<IGameObject> tiles = objectsAt(InGameLayer.tile);
            for(int i=tiles.size()-1; i>=0; --i) {
                Tile tile = (Tile)(tiles.get(i));
                tile.y += diff;
                if(tile.collider.getTop() > Metrics.height) {
                    remove(tile);
                }
            }
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
