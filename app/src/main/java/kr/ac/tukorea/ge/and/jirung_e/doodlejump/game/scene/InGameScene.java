package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.objects.Bullet;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.objects.Score;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.VertScrollBackground;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.scene.Scene;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.objects.MapLoader;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.monster.Monster;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player.Player;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.state.GameOverState;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.state.IGameState;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.state.InGameState;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile.Tile;

public class InGameScene extends Scene {
    private static final String TAG = InGameScene.class.getSimpleName();
    public Player player;
    public Score score;
    public MapLoader mapLoader;
    public final float MIN_HEIGHT;
    public static final float GRAVITY = 9.8f * 256f;
    public VertScrollBackground background = new VertScrollBackground(R.mipmap.background);
    private IGameState gameState = null;


    ///////////////////////////////////////// Constructors /////////////////////////////////////////
    public InGameScene() {
        MIN_HEIGHT = Metrics.height / 2.0f;

        initLayers(Layer.COUNT);

        gameState = new InGameState(this);
        gameState.enter();
    }


    //////////////////////////////////////////// Methods ///////////////////////////////////////////
    @Override
    public void update() {
        gameState.update();
    }

    public void superUpdate() {
        super.update();
    }

    public void shoot() {
        if(player.shoot()) {
            Bullet bullet = getObject(Bullet.class);
            bullet.x = player.x;
            bullet.y = player.y;
            add(bullet);
        }
    }

    public void scrollUp(float dy) {
        mapLoader.y += dy;
        background.moveY(dy/4);

        ArrayList<IGameObject> tiles = objectsAt(Layer.tile);
        for(int i=tiles.size()-1; i>=0; --i) {
            Tile tile = (Tile)(tiles.get(i));
            tile.y += dy;
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
        ArrayList<IGameObject> monsters = objectsAt(Layer.enemy);
        for(int i=monsters.size()-1; i>=0; --i) {
            Monster monster = (Monster)(monsters.get(i));
            monster.y += dy;
            float monster_y = monster.collider.getTop();
            if(monster_y > Metrics.height) {
                monster.collider.isActive = false;
                remove(monster);
            }
        }
    }

    public void scrollDown(float dy) {
        background.moveY(dy/4);

        ArrayList<IGameObject> tiles = objectsAt(Layer.tile);
        for(int i=tiles.size()-1; i>=0; --i) {
            Tile tile = (Tile)(tiles.get(i));
            tile.y += dy;
            float tile_y = tile.collider.getBottom();
            if(tile_y < 0) {
                tile.collider.isActive = false;
                remove(tile);
            }
        }
        ArrayList<IGameObject> monsters = objectsAt(Layer.enemy);
        for(int i=monsters.size()-1; i>=0; --i) {
            Monster monster = (Monster)(monsters.get(i));
            monster.y += dy;
            float monster_y = monster.collider.getTop();
            if(monster_y > Metrics.height) {
                monster.collider.isActive = false;
                remove(monster);
            }
        }
    }

    public void gameOver() {
        gameState = new GameOverState(this);
        gameState.enter();
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
            canvas.drawText("SCORE: " + score.getScore(), 0f, 40f, scorePaint);
            canvas.drawText("difficulty: " + mapLoader.getDifficulty(), 0f, 80f, scorePaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(gameState.onTouchEvent(event)) {
            return true;
        }

        return super.onTouchEvent(event);
    }


    @Override
    public boolean onBackPressed() {
        pause();
        return true;
    }

    public void pause() {
        GameView.view.pushScene(new PauseScene());
    }

    public void restart() {
        GameView.view.popScene();
        GameView.view.pushScene(new InGameScene());
    }
}
