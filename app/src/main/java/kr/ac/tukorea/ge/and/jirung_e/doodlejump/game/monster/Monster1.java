package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.monster;

import android.graphics.Rect;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;

public class Monster1 extends Monster {
    private static final Rect srcRect = new Rect(295, 530, 390, 602);

    @Override
    protected Rect getSrcRect() {
        return srcRect;
    }

    @Override
    protected float getSize() {
        return Metrics.width / 6f;
    }
}
