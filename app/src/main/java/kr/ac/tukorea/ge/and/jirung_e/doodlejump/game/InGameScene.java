package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game;

import android.view.MotionEvent;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.ILayerProvider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IRecyclable;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.ObjectRecycler;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.scene.Scene;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.physics.CcdResult;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;

public class InGameScene extends Scene {
    private Player player;


    ///////////////////////////////////////// Constructors /////////////////////////////////////////
    public InGameScene() {
        Metrics.setGameSize(900, 1600);

        initLayers(InGameLayer.COUNT);

        player = new Player();

        Tile tile = new Tile(Metrics.width / 2, Metrics.height * 0.8f);
        add(tile);

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
                if (prev_y < top) {
                    player.y = top;
                    player.jump();
                }
            }
            else {
                player.y = player.y + player.dy * GameView.frameTime * nearest_t;
                player.jump();
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
