package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.objects;

import android.graphics.Canvas;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.ILayerProvider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IRecyclable;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.physics.BoxCollider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.Sprite;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.InGameScene;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.Layer;

public class Bullet implements IGameObject, ILayerProvider<Layer>, IRecyclable {
    public float x, y;
    public BoxCollider collider;
    private Sprite sprite;
    public static final float SPEED = -InGameScene.GRAVITY * 1.2f;
    private static final float SIZE = Metrics.width / 9.0f * 1.8f * 0.2f;


    public Bullet() {
        this(0, 0);
    }

    public Bullet(float x, float y) {
        this.x = x;
        this.y = y;
        this.collider = new BoxCollider(x, y, SIZE, SIZE);
        this.sprite = new Sprite(R.mipmap.bullet);
        this.sprite.setSize(SIZE, SIZE);
        this.sprite.setPosition(x, y);
    }

    @Override
    public void update() {
        y += SPEED * GameView.frameTime;
        collider.setPosition(x, y);
        sprite.setPosition(x, y);
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public BoxCollider getCollider() {
        return collider;
    }

    @Override
    public Layer getLayer() {
        return Layer.bullet;
    }

    @Override
    public void onRecycle() {
        collider.isActive = true;
    }
}
