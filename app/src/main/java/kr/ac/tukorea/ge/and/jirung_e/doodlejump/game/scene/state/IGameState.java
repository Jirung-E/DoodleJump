package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.state;

import android.view.MotionEvent;


public interface IGameState {
    void enter();
    void update();
    void exit();
    boolean onTouchEvent(MotionEvent event);
}
