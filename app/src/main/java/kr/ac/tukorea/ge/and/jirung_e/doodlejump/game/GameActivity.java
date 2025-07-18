package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.BuildConfig;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.GameView;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.InGameScene;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.scene.LobbyScene;


public class GameActivity extends AppCompatActivity {
    private GameView gameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Metrics.setGameSize(900, 1600);
        GameView.drawsDebugStuffs = BuildConfig.DEBUG;
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        gameView.pushScene(new LobbyScene());
//        gameView.pushScene(new InGameScene());
        setContentView(gameView);

        gameView.setEmptyStackListener(new GameView.OnEmptyStackListener() {
            @Override
            public void onEmptyStack() {
                finishAffinity();
            }
        });
        getOnBackPressedDispatcher().addCallback(onBackPressedCallback);

        setFullScreen();
    }


    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            gameView.onBackPressed();
        }
    };

    @SuppressWarnings("deprecation")
    public void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // API 30 이상: 최신 방식
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                );
                insetsController.hide(WindowInsets.Type.systemBars());
            }
        } else {
            // API 29 이하: 기존 방식
            int flags = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            gameView.setSystemUiVisibility(flags);
        }
    }
}