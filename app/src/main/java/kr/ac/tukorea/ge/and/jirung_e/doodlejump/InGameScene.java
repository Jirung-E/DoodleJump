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

        float nearest_t = Float.POSITIVE_INFINITY;
        float top = prev_y;
        for(Tile tile : tiles) {
            // 아래로 내려가는 중에 충돌하는 경우
            CcdResult result = player.collider.ccd(tile.collider, player.dx * GameView.frameTime, player.dy * GameView.frameTime);
            if(player.dy > 0 && result.isCollide) {
                if(result.t < nearest_t) {
                    if(result.ny > 0) {
                        nearest_t = result.t;
                    }
                    if(result.t == 0) {
                        nearest_t = result.t;
                        top = tile.collider.getTop();
                    }
                }
            }
        }
        if(nearest_t < Float.POSITIVE_INFINITY) {
            if(nearest_t == 0) {
                // 이전프레임에 타일보다 위에 있던 경우
                if (prev_y < top) {
                    player.y = top;
                    player.jump();
                }
            }
            else {
                player.y = player.y + player.dy * GameView.frameTime * nearest_t;
                player.jump();
            }
        }
    }
}
