package kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.BuildConfig;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.scene.Scene;


public class GameView extends View implements Choreographer.FrameCallback {
    private static final String TAG = GameView.class.getSimpleName();
    private static long previousNanos;
    public static float frameTime;
    public static GameView view;
    public static boolean drawsDebugStuffs = false;
    private ArrayList<Scene> sceneStack = new ArrayList<>();
    public interface OnEmptyStackListener {
        public void onEmptyStack();
    }
    private OnEmptyStackListener emptyStackListener;
    public void setEmptyStackListener(OnEmptyStackListener emptyStackListener) {
        this.emptyStackListener = emptyStackListener;
    }


    ///////////////////////////////////////// Constructor /////////////////////////////////////////
    public GameView(Context context) {      // AttributeSet - 미리보기를 위해 적어두기
        super(context);

        GameView.view = this;
        scheduleUpdate();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////


    private void update() {
        Scene scene = getTopScene();
        if (scene != null) {
            scene.update();
        }
    }


    public void pushScene(Scene scene) {
        int last = sceneStack.size() - 1;
        if (last >= 0) {
            sceneStack.get(last).onPause();
        }
        sceneStack.add(scene);
        scene.onEnter();
    }
    public Scene popScene() {
        int last = sceneStack.size() - 1;
        if (last < 0) {
            notifyEmptyStack();
            return null;
        }
        Scene top = sceneStack.remove(last);
        top.onExit();
        if (last >= 1) {
            sceneStack.get(last - 1).onResume();
        } else {
            notifyEmptyStack();
        }
        return top;
    }

    private void notifyEmptyStack() {
        if (emptyStackListener != null) {
            emptyStackListener.onEmptyStack();
        }
    }

    public void changeScene(Scene scene) {
        int last = sceneStack.size() - 1;
        if (last < 0) return;
        sceneStack.get(last).onExit();
        sceneStack.add(scene);
        scene.onEnter();
    }
    public Scene getTopScene() {
        //return sceneStack.getLast();
        // Call requires API level 35 (current min is 24): java. util. ArrayList#getLast
        int last = sceneStack.size() - 1;
        if (last < 0) return null;
        return sceneStack.get(last);
    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        Metrics.concat(canvas);

        if (drawsDebugStuffs) {
            drawDebugBackground(canvas);
        }

        Scene scene = getTopScene();
        if (scene != null) {
            scene.draw(canvas);
        }

        canvas.restore();
        if (drawsDebugStuffs) {
            drawDebugInfo(canvas, scene);
        }
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Metrics.onSize(w, h);
    }

    @Override
    public void doFrame(long nanos) {
        if (previousNanos != 0) {
            frameTime = (nanos - previousNanos) / 1_000_000_000f;
            update();
            invalidate();
        }
        previousNanos = nanos;
        if (isShown()) {
            scheduleUpdate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Scene scene = getTopScene();
        if (scene != null) {
            return scene.onTouchEvent(event);
        }

        return super.onTouchEvent(event);
    }

    public void onBackPressed() {
        int last = sceneStack.size() - 1;
        if (last < 0) return; // finish activity here ?

        Scene scene = sceneStack.get(last);
        boolean handled = scene.onBackPressed();
        if (handled) return;

        popScene();
    }

    private void scheduleUpdate() {
        Choreographer.getInstance().postFrameCallback(this);
    }


    //////////////////////////////////////////// DEBUG ////////////////////////////////////////////
    private Paint borderPaint, gridPaint, fpsPaint;
    private void drawDebugBackground(@NonNull Canvas canvas) {
        if (borderPaint == null) {
            borderPaint = new Paint();
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(10f);
            borderPaint.setColor(Color.RED);

            gridPaint = new Paint();
            gridPaint.setStyle(Paint.Style.STROKE);
            gridPaint.setStrokeWidth(1f);
            gridPaint.setColor(Color.GRAY);
        }

        canvas.drawRect(Metrics.borderRect, borderPaint);
        for (float x = Metrics.GRID_UNIT; x < Metrics.width; x += Metrics.GRID_UNIT) {
            canvas.drawLine(x, 0, x, Metrics.height, gridPaint);
        }
        for (float y = Metrics.GRID_UNIT; y < Metrics.height; y += Metrics.GRID_UNIT) {
            canvas.drawLine(0, y, Metrics.width, y, gridPaint);
        }
    }

    private void drawDebugInfo(Canvas canvas, Scene scene) {
        if (fpsPaint == null) {
            fpsPaint = new Paint();
            fpsPaint.setColor(Color.BLUE);
            fpsPaint.setTypeface(Typeface.MONOSPACE);
            fpsPaint.setTextSize(80f);
        }

        int fps = (int) (1.0f / frameTime);
        int count = scene != null ? scene.count() : 0;
        String countsForLayers = scene != null ? scene.getDebugCounts() : "";
        canvas.drawText("FPS: " + fps, 80f, 80f, fpsPaint);
        canvas.drawText(count + " " + countsForLayers, 80f, 160f, fpsPaint);
    }
}