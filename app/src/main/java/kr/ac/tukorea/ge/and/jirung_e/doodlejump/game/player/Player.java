package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import androidx.annotation.NonNull;

import java.util.HashMap;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.ILayerProvider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.BitmapPool;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.Sprite;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.physics.BoxCollider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.objects.Bullet;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.Layer;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.InGameScene;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.item.ItemId;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player.booster.IBooster;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player.booster.Jetpack;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player.booster.Propeller;


public class Player implements IGameObject, ILayerProvider<Layer> {
    private static final String TAG = Player.class.getSimpleName();
    private static final float WIDTH = Metrics.width / 9.0f * 1.8f;
    private static final float WIDTH_HALF = WIDTH / 2;
    private final float COLLIDER_OFFSET_Y;
    public float x, y;
    public float dx, dy;
    private float targetDx;

    public BoxCollider collider;
    private static final float JUMP_SPEED = -InGameScene.GRAVITY * 0.6f;
    private static final float MAX_SPEED = InGameScene.GRAVITY;
    private static final float MOVE_SPEED = Metrics.width;
    private static final float ACCELERATION_X = 4 * MOVE_SPEED;
    private Sprite sprite;
    private Sprite cannonSprite;
    private final HashMap<Integer, Sprite> sprites;
    private IBooster booster;

    private int action;
    private static final float CROUCH_TIME_MAX = 0.5f;
    private static final float ATTACK_TIME_MAX = 0.5f;
    private float crouchTime;
    private float attackTime;
    private float boostPower;
    private float boostTime;
    public boolean isAlive = true;


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
        sprites.put(Action.LEFT | Action.CROUCH, sprite);

        sprite = new Sprite(R.mipmap.character_right);
        sprite.setOffset(0.0f, -HEIGHT_HALF);
        sprite.setSize(WIDTH, HEIGHT);
        sprites.put(Action.RIGHT, sprite);

        sprite = new Sprite(R.mipmap.character_right_crouch);
        sprite.setOffset(0.0f, -HEIGHT_HALF * 1.12f);
        sprite.setSize(WIDTH, HEIGHT);
        sprites.put(Action.RIGHT | Action.CROUCH, sprite);

        sprite = new Sprite(R.mipmap.character_attack);
        sprite.setOffset(0.0f, -HEIGHT_HALF);
        sprite.setSize(WIDTH, HEIGHT);
        sprites.put(Action.ATTACK, sprite);

        sprite = new Sprite(R.mipmap.character_attack_crouch);
        sprite.setOffset(0.0f, -HEIGHT_HALF * 1.12f);
        sprite.setSize(WIDTH, HEIGHT);
        sprites.put(Action.ATTACK | Action.CROUCH, sprite);

        action = Action.RIGHT | Action.CROUCH;
        sprite = sprites.get(action);

        cannonSprite = new Sprite(R.mipmap.character_cannon);
        float cannonWidth = HEIGHT * cannonSprite.getBitmap().getWidth() / cannonSprite.getBitmap().getHeight();
        cannonSprite.setOffset(0.0f, -HEIGHT_HALF * 1f);
        cannonSprite.setSize(cannonWidth, HEIGHT);
        cannonSprite.setPosition(0, 0);

        x = Metrics.width / 2;
        y = Metrics.height;
        dx = 0;
        dy = JUMP_SPEED;
        targetDx = 0;

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
        dx += Math.signum(targetDx - dx) * ACCELERATION_X * GameView.frameTime;
        if(targetDx == 0 && prev_dx_sign != Math.signum(dx)) {
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
                booster = null;
            }
        }
        else {
            dy = Math.min(dy + InGameScene.GRAVITY * GameView.frameTime, MAX_SPEED);
        }

        x += dx * GameView.frameTime;
        y += dy * GameView.frameTime;

        updateCollider();
        updateSprite();

        if(booster != null) {
            booster.update(x, y, action, GameView.frameTime);
        }
    }

    public Bullet shoot() {
        if(booster != null) {
            return null;
        }

        attackTime = ATTACK_TIME_MAX;
        action |= Action.ATTACK;
        sprite = getSprite();
        sprite.setPosition(x, y);
        cannonSprite.setPosition(x, y);
        return new Bullet(x, y, JUMP_SPEED * 2, WIDTH * 0.2f);
    }

    public void equip(@NonNull ItemId item_id) {
        switch(item_id) {
            case PROPELLER:
                booster = new Propeller();
                boost();
                break;
            case JETPACK:
                booster = new Jetpack();
                boost();
                break;
            default:
                throw new IllegalArgumentException("Unknown item: " + item_id);
        }
    }

    private void boost() {
        collider.isActive = false;
        boostPower = booster.getBoostPower() * JUMP_SPEED;
        boostTime = booster.getBoostTime();
    }

    public void jump() {
        jump(1.0f);
    }

    public void jump(float power) {
        dy = JUMP_SPEED * power;
        action |= Action.CROUCH;
        sprite = getSprite();
        sprite.setPosition(x, y);
        cannonSprite.setPosition(x, y);
        crouchTime = 0;
    }

    public void stun() {
        isAlive = false;
        collider.isActive = false;
        dy = 0;
    }

    /// 크기와 상관 없이 이동방향 설정
    /// - 양수: 오른쪽
    /// - 음수: 왼쪽
    /// - 0: 정지
    public void setTargetMoveDirection(int dx) {
        if(!isAlive) return;

        targetDx = Math.signum(dx) * MOVE_SPEED;
        int prevDirection = action & Action.DIRECTION_MASK;
        action &= ~Action.DIRECTION_MASK;
        if(targetDx > 0) {
            action |= Action.RIGHT;
        }
        else if(targetDx < 0) {
            action |= Action.LEFT;
        }
        else {
            action |= prevDirection;
        }
        if((action & Action.ATTACK) != Action.ATTACK) {
            sprite = getSprite();
            sprite.setPosition(x, y);
            cannonSprite.setPosition(x, y);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        if((action & Action.ATTACK) == Action.ATTACK) {
            cannonSprite.draw(canvas);
        }
        if(GameView.drawsDebugStuffs) {
            collider.draw(canvas);
        }
        if(booster != null) {
            booster.draw(canvas);
        }
    }

    private void updateCollider() {
        collider.setPosition(x, y - COLLIDER_OFFSET_Y);
    }

    private void updateSprite() {
        crouchTime += GameView.frameTime;
        if(crouchTime >= CROUCH_TIME_MAX) {
            action &= ~Action.CROUCH;
            sprite = getSprite();
        }
        if(attackTime > 0) {
            attackTime -= GameView.frameTime;
            if(attackTime <= 0) {
                action &= ~Action.ATTACK;
                sprite = getSprite();
            }
        }

        sprite.setPosition(x, y);
        cannonSprite.setPosition(x, y);
    }

    private Sprite getSprite() {
        int idx = action;
        if((action & Action.ATTACK) == Action.ATTACK) {
            idx &= ~Action.DIRECTION_MASK;
        }
        else {
            idx &= ~Action.ATTACK;
        }
        return sprites.get(idx);
    }


    @Override
    public Layer getLayer() {
        return Layer.player;
    }
}
