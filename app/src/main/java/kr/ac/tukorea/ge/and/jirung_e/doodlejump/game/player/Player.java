package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.HashMap;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.ILayerProvider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.BitmapPool;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.Sprite;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.physics.BoxCollider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.InGameLayer;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.InGameScene;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.item.Booster;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player.equipment.Equipment;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player.equipment.Jetpack;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player.equipment.Propeller;


public class Player implements IGameObject, ILayerProvider<InGameLayer> {
    private static final String TAG = Player.class.getSimpleName();
    private static final float WIDTH = Metrics.width / 9.0f * 1.8f;
    private static final float WIDTH_HALF = WIDTH / 2;
    private final float COLLIDER_OFFSET_Y;
    public float x, y;
    public float dx, dy;
    private float target_dx;

    public BoxCollider collider;
    private static final float JUMP_SPEED = -InGameScene.GRAVITY * 0.6f;
    private static final float MOVE_SPEED = Metrics.width;
    private static final float ACCELERATION_X = 4 * MOVE_SPEED;
    private Sprite sprite;
    private final HashMap<Action, Sprite> sprites;
    private Equipment equipment;

    private Action action;
    private static final float crouchTimeMax = 0.5f;
    private float crouchTime;
    private float boostPower;
    private float boostTime;


    ///////////////////////////////////////// Constructors /////////////////////////////////////////
    public Player() {
        sprites = new HashMap<>();

        Bitmap bitmap = BitmapPool.get(R.mipmap.character_left);
        float HEIGHT = WIDTH * ((float) bitmap.getHeight() / bitmap.getWidth());
        float HEIGHT_HALF = HEIGHT / 2;

        sprite = new Sprite(bitmap);
        sprite.setOffset(0.0f, -HEIGHT_HALF);
        sprite.setSize(WIDTH, HEIGHT);
        sprites.put(Action.LEFT, sprite);

        sprite = new Sprite(R.mipmap.character_left_crouch);
        sprite.setOffset(0.0f, -HEIGHT_HALF * 1.12f);
        sprite.setSize(WIDTH, HEIGHT);
        sprites.put(Action.LEFT_CROUCH, sprite);

        sprite = new Sprite(R.mipmap.character_right);
        sprite.setOffset(0.0f, -HEIGHT_HALF);
        sprite.setSize(WIDTH, HEIGHT);
        sprites.put(Action.RIGHT, sprite);

        sprite = new Sprite(R.mipmap.character_right_crouch);
        sprite.setOffset(0.0f, -HEIGHT_HALF * 1.12f);
        sprite.setSize(WIDTH, HEIGHT);
        sprites.put(Action.RIGHT_CROUCH, sprite);
        action = Action.RIGHT_CROUCH;

        x = Metrics.width / 2;
        y = Metrics.height;
        dx = 0;
        dy = JUMP_SPEED;
        target_dx = 0;

        float COLLIDER_HEIGHT = HEIGHT * 0.8f;
        COLLIDER_OFFSET_Y = COLLIDER_HEIGHT / 2;
        collider = new BoxCollider(WIDTH_HALF, COLLIDER_HEIGHT);

        updateCollider();
        sprite.setPosition(x, y);
    }


    //////////////////////////////////////////// Methods ///////////////////////////////////////////
    @Override
    public void update() {
        float prev_dx_sign = Math.signum(dx);
        dx += Math.signum(target_dx - dx) * ACCELERATION_X * GameView.frameTime;
        if(target_dx == 0 && prev_dx_sign != Math.signum(dx)) {
            dx = 0;
        }
        dx = Math.clamp(dx, -MOVE_SPEED, MOVE_SPEED);

        if(boostTime > 0) {
            // boostPower까지 서서히 속도 증가
            dy = Math.max(dy + boostPower * GameView.frameTime, boostPower);
            boostTime -= GameView.frameTime;
            if(boostTime <= 0) {
                boostTime = 0;
                collider.isActive = true;
                equipment = null;
            }
        }
        else {
            dy = Math.min(dy + InGameScene.GRAVITY * GameView.frameTime, -JUMP_SPEED);
        }

        x += dx * GameView.frameTime;
        y += dy * GameView.frameTime;

        updateCollider();
        updateSprite();

        if(equipment != null) {
            equipment.update(x, y, action, GameView.frameTime);
        }
    }

    public void boost(Booster booster) {
        collider.isActive = false;
        boostPower = booster.getBoostPower() * JUMP_SPEED;
        boostTime = booster.getBoostTime();
        equipment = new Propeller();
    }

    public void jump() {
        jump(1.0f);
    }

    public void jump(float power) {
        dy = JUMP_SPEED * power;
        if(action == Action.LEFT) {
            action = Action.LEFT_CROUCH;
        }
        else if(action == Action.RIGHT) {
            action = Action.RIGHT_CROUCH;
        }
        sprite = sprites.get(action);
        sprite.setPosition(x, y);
        crouchTime = 0;
    }

    /// 크기와 상관 없이 이동방향 설정
    /// - 양수: 오른쪽
    /// - 음수: 왼쪽
    /// - 0: 정지
    public void setTargetMoveDirection(int dx) {
        target_dx = Math.signum(dx) * MOVE_SPEED;
        if(crouchTime < crouchTimeMax) {
            if (dx < 0) {
                action = Action.LEFT_CROUCH;
            } else if (dx > 0) {
                action = Action.RIGHT_CROUCH;
            }
        }
        else {
            if (dx < 0) {
                action = Action.LEFT;
            } else if (dx > 0) {
                action = Action.RIGHT;
            }
        }
        sprite = sprites.get(action);
        sprite.setPosition(x, y);
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        if(GameView.drawsDebugStuffs) {
            collider.draw(canvas);
        }
        if(equipment != null) {
            equipment.draw(canvas);
        }
    }

    private void updateCollider() {
        collider.setPosition(x, y - COLLIDER_OFFSET_Y);
    }

    private void updateSprite() {
        crouchTime += GameView.frameTime;
        if(crouchTime >= crouchTimeMax) {
            if(action == Action.LEFT_CROUCH) {
                action = Action.LEFT;
            }
            else if(action == Action.RIGHT_CROUCH) {
                action = Action.RIGHT;
            }
            sprite = sprites.get(action);
        }

        sprite.setPosition(x, y);
    }


    @Override
    public InGameLayer getLayer() {
        return InGameLayer.player;
    }
}
