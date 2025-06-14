package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.state;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.Button;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.physics.CcdResult;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.Sprite;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.util.RectUtil;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.item.Item;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.item.Spring;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.monster.Monster;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.objects.Bullet;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.objects.MapLoader;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.objects.Score;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player.Player;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.InGameScene;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.Layer;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile.BrokenTile;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile.NormalTile;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.tile.Tile;


public class InGameState implements IGameState {
    private final InGameScene scene;
    private Button pauseButton;
    private Button shootButton;
    private RectF shootButtonArea;
    private IGameObject shootButtonObj;
    private static final RectF touchArea = new RectF(0, Metrics.height / 2, Metrics.width, Metrics.height);


    public InGameState(InGameScene scene) {
        this.scene = scene;
    }


    @Override
    public void enter() {
        scene.mapLoader = new MapLoader(scene);
        scene.add(scene.mapLoader);

        scene.player = new Player();

        Tile tile = new NormalTile(Metrics.width / 2, Metrics.height * 0.95f);
        scene.add(tile);

        scene.add(scene.player);

        scene.background.addDistanceY(-8);
        scene.add(Layer.bg, scene.background);

        Sprite scoreBarSprite = new Sprite(R.mipmap.top_score);
        Rect scoreBarSrcRect = new Rect(0, 0, 640, 92);
        float scoreBarHeight = Metrics.width * (float)scoreBarSrcRect.height() / scoreBarSrcRect.width();
        scoreBarSprite.setSrcRect(scoreBarSrcRect);
        scoreBarSprite.setSize(Metrics.width, scoreBarHeight);
        scoreBarSprite.setPosition(Metrics.width / 2, scoreBarHeight / 2);
        IGameObject scoreBar = new IGameObject() {
            @Override
            public void update() {

            }

            @Override
            public void draw(Canvas canvas) {
                scoreBarSprite.draw(canvas);
            }
        };
        scene.add(Layer.ui, scoreBar);

        scene.score = new Score(scoreBarHeight / 8, scoreBarHeight / 2, scoreBarHeight / 3);
        scene.score.setScore(0);
        scene.add(Layer.ui, scene.score);

        Rect pauseButtonSrcRect = new Rect(911, 0, 971, 40);
        float buttonHeight = 40f / scoreBarSrcRect.height() * scoreBarHeight;
        float buttonWidth = buttonHeight * (pauseButtonSrcRect.width() / 2f / pauseButtonSrcRect.height());
        Sprite pauseButtonSprite = new Sprite(R.mipmap.top_score);
        Sprite pauseButtonPressedSprite = new Sprite(R.mipmap.top_score);
        pauseButtonSprite.setSrcRect(911, 0, pauseButtonSrcRect.centerX(), 40);
        pauseButtonPressedSprite.setSrcRect(pauseButtonSrcRect.centerX(), 0, 971, 40);
        pauseButtonSprite.setSize(buttonWidth, buttonHeight);
        pauseButtonPressedSprite.setSize(buttonWidth, buttonHeight);

        pauseButton = new Button(
                pauseButtonSprite,
                pauseButtonPressedSprite,
                new Button.OnTouchListener() {
                    @Override
                    public boolean onTouch(boolean pressed) {
                        if(!pressed) {
                            scene.pause();
                        }
                        return true;
                    }
                }
        );
        pauseButton.setPosition(Metrics.width - buttonWidth * 1.7f, buttonHeight * 0.8f);

        scene.addController(pauseButton);

        IGameObject pauseButtonObj = new IGameObject() {
            @Override
            public void update() {

            }

            @Override
            public void draw(Canvas canvas) {
                pauseButton.draw(canvas);
            }
        };
        scene.add(Layer.ui, pauseButtonObj);

        Sprite shootButtonSprite = new Sprite(R.mipmap.shoot_button);
        float shootButtonWidth = Metrics.width * 0.4f;
        float shootButtonHeight = shootButtonWidth * (float)shootButtonSprite.getBitmap().getHeight() / shootButtonSprite.getBitmap().getWidth();
        shootButtonSprite.setSize(shootButtonWidth, shootButtonHeight);
        shootButton = new Button(
                shootButtonSprite,
                shootButtonSprite,
                new Button.OnTouchListener() {
                    @Override
                    public boolean onTouch(boolean pressed) {
                        if(pressed) {
                            scene.shoot();
                        }
                        return true;
                    }
                }
        );
        float shootButtonX = Metrics.width / 2;
        float shootButtonY = Metrics.height - shootButtonHeight * 0.8f;
        shootButton.setPosition(shootButtonX, shootButtonY);
        shootButtonArea = RectUtil.newRectF(shootButtonX, shootButtonY, shootButtonWidth, shootButtonHeight);

        shootButtonObj = new IGameObject() {
            @Override
            public void update() {

            }

            @Override
            public void draw(Canvas canvas) {
                shootButton.draw(canvas);
            }
        };
        scene.add(Layer.ui, shootButtonObj);
    }


    @Override
    public void update() {
        // Player
        float nearest_t = Float.POSITIVE_INFINITY;
        IGameObject collidee = null;
        CcdResult nearest_result = null;
        for(IGameObject obj : scene.objectsAt(Layer.tile)) {
            Tile tile = (Tile)obj;
            // 아래로 내려가는 중에 충돌하는 경우
            if(scene.player.dy > 0) {
                CcdResult result = scene.player.collider.ccd(tile.collider, scene.player.dx * GameView.frameTime, scene.player.dy * GameView.frameTime);
                if(result.isCollide) {
                    if(-0.1f <= result.t && result.t < nearest_t) {
                        if(result.ny < 0) {
                            nearest_t = result.t;
                            collidee = tile;
                            nearest_result = result;
                        }
                    }
                }
            }
        }
        for(IGameObject obj : scene.objectsAt(Layer.enemy)) {
            Monster monster = (Monster)obj;
            // 아래로 내려가는 중에 충돌하는 경우
            CcdResult result = scene.player.collider.ccd(monster.collider, scene.player.dx * GameView.frameTime, scene.player.dy * GameView.frameTime);
            if(result.isCollide) {
                if(-0.1f <= result.t && result.t < nearest_t) {
                    nearest_t = result.t;
                    collidee = monster;
                    nearest_result = result;
                }
            }
        }
        for(IGameObject obj : scene.objectsAt(Layer.item)) {
            Item item = (Item)obj;
            CcdResult result = scene.player.collider.ccd(item.collider, scene.player.dx * GameView.frameTime, scene.player.dy * GameView.frameTime);
            if(result.isCollide) {
                if(-0.1f <= result.t && result.t < nearest_t) {
                    if(item instanceof Spring) {
                        if(scene.player.dy > 0) {
                            if(result.ny < 0) {
                                nearest_t = result.t;
                                collidee = item;
                                nearest_result = result;
                            }
                        }
                    }
                    else {
                        nearest_t = result.t;
                        collidee = item;
                        nearest_result = result;
                    }
                }
            }
        }
        if(nearest_result != null) {
            if(collidee instanceof Tile) {
                // 여러개 겹쳐있는 경우 하나만 처리하게 되지만, 겹쳐있게 만들지 않을거임
                if(collidee instanceof BrokenTile) {
                    ((BrokenTile)collidee).trigger();
                }
                else {
                    scene.player.y = scene.player.y + scene.player.dy * GameView.frameTime * nearest_t;
                    scene.player.jump();
                }
            }
            else if(collidee instanceof Monster) {
                if(nearest_result.ny < 0 && scene.player.dy > 0) {
                    // 몬스터를 밟은 경우
                    scene.player.y = scene.player.y + scene.player.dy * GameView.frameTime * nearest_t;
                    scene.player.jump();
                    ((Monster)collidee).die();
                }
                else {
                    // 몬스터와 충돌한 경우
                    scene.player.stun();
                }
            }
            else if(collidee instanceof Item) {
                if(collidee instanceof Spring) {
                    scene.player.y = scene.player.y + scene.player.dy * GameView.frameTime * nearest_t;
                    scene.player.jump(2);
                    ((Spring)collidee).trigger();
                }
                else {
                    Item item = (Item)collidee;
                    scene.player.equip(item.getId());
                    scene.remove(item);
                }
            }
        }

        nearest_result = null;

        // Bullet
        Bullet collided_bullet = null;
        for(IGameObject obj : scene.objectsAt(Layer.bullet)) {
            Bullet bullet = (Bullet)obj;
            for(IGameObject m : scene.objectsAt(Layer.enemy)) {
                Monster monster = (Monster)m;
                CcdResult result = bullet.collider.ccd(monster.collider, 0, Bullet.SPEED * GameView.frameTime);
                if(result.isCollide) {
                    if(-0.1f <= result.t && result.t < nearest_t) {
                        nearest_t = result.t;
                        collidee = monster;
                        nearest_result = result;
                        collided_bullet = bullet;
                    }
                }
            }
        }
        if(nearest_result != null) {
            // 몬스터와 충돌
            ((Monster)collidee).die();
            scene.remove(collided_bullet);
        }

        scene.superUpdate();

        if(scene.player.x < 0) {
            scene.player.x = Metrics.width;
        }
        else if(scene.player.x > Metrics.width) {
            scene.player.x = 0;
        }

        // 일정 높이 이상으로 올라가면 타일 등의 오브젝트를 아래로 이동시킴 + 스코어 증가
        if(scene.player.y < scene.MIN_HEIGHT) {
            float diff = scene.MIN_HEIGHT - scene.player.y;

            scene.player.y = scene.MIN_HEIGHT;
            scene.score.add(diff / 10);
            scene.mapLoader.setDifficulty(scene.score.getScore() / 5000.0f);

            scene.scrollUp(diff);
        }
        else if(scene.player.collider.getTop() > Metrics.height) {
            // 일반적인 상황에서 떨어지거나 몬스터와 충돌하여 죽은 경우
            // (아이템을 획득한 상태에서는 죽지 않음)
            if(scene.player.collider.isActive || !scene.player.isAlive) {
                exit();
            }
        }

        // Bullet remove
        for(int i = scene.objectsAt(Layer.bullet).size() - 1; i >= 0; --i) {
            IGameObject obj = scene.objectsAt(Layer.bullet).get(i);
            Bullet bullet = (Bullet)obj;
            if(bullet.collider.getBottom() < 0 || bullet.collider.getTop() > Metrics.height) {
                scene.remove(bullet);
            }
        }
    }

    @Override
    public void exit() {
        scene.gameOver();
        scene.removeController(pauseButton);
        scene.remove(Layer.ui, shootButtonObj);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float[] pts = Metrics.fromScreen(event.getX(), event.getY());

        if(shootButtonArea.contains(pts[0], pts[1])) {
            return shootButton.onTouchEvent(event);
        }

        if(touchArea.contains(pts[0], pts[1])) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    if(pts[0] >= Metrics.width / 2) {
                        scene.player.setTargetMoveDirection(1);
                    }
                    else {
                        scene.player.setTargetMoveDirection(-1);
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    scene.player.setTargetMoveDirection(0);
                    return true;
            }
            return false;
        }

        return false;
    }
}
