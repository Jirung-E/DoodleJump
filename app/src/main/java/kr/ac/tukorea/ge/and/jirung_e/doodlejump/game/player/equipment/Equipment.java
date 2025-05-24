package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player.equipment;

import android.graphics.Canvas;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player.Action;

public abstract class Equipment {
    public abstract void update(float x, float y, Action action, float frameTime);
    public abstract void draw(Canvas canvas);
}
