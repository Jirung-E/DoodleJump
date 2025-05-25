package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player.booster;

import android.graphics.Canvas;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player.Action;

public interface IBooster {
    void update(float x, float y, Action action, float frameTime);
    void draw(Canvas canvas);

    float getBoostPower();
    float getBoostTime();
}
