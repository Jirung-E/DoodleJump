package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.objects;

import android.graphics.Canvas;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.R;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.physics.BoxCollider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.resource.Sprite;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;

public class Bullet implements IGameObject {
    private float x, y;
    private float speedY;
    private BoxCollider collider;
    private Sprite sprite;


    public Bullet(float x, float y, float speedY, float size) {
        this.x = x;
        this.y = y;
        this.speedY = speedY;
        this.collider = new BoxCollider(x, y, size, size);
        this.sprite = new Sprite(R.mipmap.bullet);
        this.sprite.setSize(size, size);
        this.sprite.setPosition(x, y);
    }

    @Override
    public void update() {
        y += speedY * GameView.frameTime;
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
}
