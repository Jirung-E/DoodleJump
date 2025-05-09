package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game;

import java.util.Random;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;

public class TileLoader {
    private final InGameScene scene;
    private final Random random = new Random();

    public TileLoader(InGameScene mainScene) {
        this.scene = mainScene;
    }

    public void update() {
        // 일정 간격마다 랜덤한 x값을 가지고 타일을 생성
    }
}
