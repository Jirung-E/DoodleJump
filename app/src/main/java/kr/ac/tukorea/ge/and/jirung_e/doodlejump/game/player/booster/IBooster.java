package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player.booster;

import android.graphics.Canvas;


public interface IBooster {
    void update(float x, float y, int action, float frameTime);
    void draw(Canvas canvas);

    float getBoostPower();
    float getBoostTime();
}
