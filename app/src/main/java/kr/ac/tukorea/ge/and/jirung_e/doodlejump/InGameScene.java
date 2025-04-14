package kr.ac.tukorea.ge.and.jirung_e.doodlejump;

import java.util.ArrayList;

public class InGameScene extends Scene {
    private Player player;
    private ArrayList<Tile> tiles = new ArrayList<>();


    public InGameScene() {
        Metrics.setGameSize(900, 1600);

        player = new Player();

        Tile tile = new Tile(Metrics.width / 2, Metrics.height * 0.8f);
        tiles.add(tile);
        gameObjects.add(tile);

        gameObjects.add(player);
    }


    @Override
    public void update() {
        float prev_y = player.y;

        super.update();

        for(Tile tile : tiles) {
            // 아래로 내려가는 중에 충돌하는 경우
            if (player.dy > 0 && player.collider.isCollide(tile.collider)) {
                float top = tile.collider.getTop();
                // 이전프레임에 타일보다 위에 있던 경우
                if (prev_y < top) {
                    player.y = top;
                    player.jump();
                }
            }
        }
    }
}
