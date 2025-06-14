package kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.scene;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IGameObject;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.ILayerProvider;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.IRecyclable;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.ITouchable;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects.ObjectRecycler;
import kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.view.Metrics;


public class Scene {
    private static final String TAG = Scene.class.getSimpleName();
    protected ArrayList<ArrayList<IGameObject>> layers = new ArrayList<>();
    protected ArrayList<ITouchable> controllers = new ArrayList<>();
    private final ObjectRecycler recycler = new ObjectRecycler();


    protected void initLayers(int layerCount) {
        layers.clear();
        for (int i = 0; i < layerCount; i++) {
            layers.add(new ArrayList<>());
        }
    }
    public <E extends Enum<E>> void add(E layer, IGameObject gameObject) {
        int layerIndex = layer.ordinal();
        ArrayList<IGameObject> gameObjects = layers.get(layerIndex);
        gameObjects.add(gameObject);
    }

    public void add(ILayerProvider<?> gameObject) {
        Enum<?> e = gameObject.getLayer();
        int layerIndex = e.ordinal();
        ArrayList<IGameObject> gameObjects = layers.get(layerIndex);
        gameObjects.add(gameObject);
    }

    public <E extends Enum<E>> void remove(E layer, IGameObject gobj) {
        int layerIndex = layer.ordinal();
        remove(layerIndex, gobj);
    }

    public void remove(ILayerProvider<?> gameObject) {
        Enum<?> e = gameObject.getLayer();
        int layerIndex = e.ordinal();
        remove(layerIndex, gameObject);
    }

    private void remove(int layerIndex, IGameObject gobj) {
        ArrayList<IGameObject> gameObjects = layers.get(layerIndex);
        gameObjects.remove(gobj);
        if (gobj instanceof IRecyclable) {
            recycler.collectRecyclable((IRecyclable) gobj);
            ((IRecyclable) gobj).onRecycle();
        }
    }

    public <E extends Enum<E>> ArrayList<IGameObject> objectsAt(E layer) {
        int layerIndex = layer.ordinal();
        return layers.get(layerIndex);
    }

    public int count() {
        int total = 0;
        for (ArrayList<IGameObject> layer : layers) {
            total += layer.size();
        }
        return total;
    }

    public String getDebugCounts() {
        StringBuilder sb = new StringBuilder();
        for (ArrayList<IGameObject> gameObjects : layers) {
            if (sb.length() == 0) {
                sb.append('[');
            } else {
                sb.append(',');
            }
            sb.append(gameObjects.size());
        }
        sb.append(']');
        return sb.toString();
    }


    public <T extends IRecyclable> T getObject(Class<T> clazz) {
        return recycler.getRecyclable(clazz);
    }


    public void update() {
        for (ArrayList<IGameObject> gameObjects : layers) {
            int count = gameObjects.size();
            for (int i = count - 1; i >= 0; i--) {
                IGameObject gobj = gameObjects.get(i);
                gobj.update();
            }
        }
    }
    public void draw(Canvas canvas) {
        if (this.clipsRect()) {
            canvas.clipRect(0, 0, Metrics.width, Metrics.height);
        }
        for (ArrayList<IGameObject> gameObjects : layers) {
            for (IGameObject gobj : gameObjects) {
                gobj.draw(canvas);
            }
        }
    }

    protected ITouchable capturingTouchable;
    public boolean onTouchEvent(MotionEvent event) {
        if (capturingTouchable != null) {
            boolean processed = capturingTouchable.onTouchEvent(event);
            if (!processed || event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(TAG, "Capture End: " + capturingTouchable);
                capturingTouchable = null;
            }
            return processed;
        }

        for (ITouchable controller : controllers) {
            boolean processed = controller.onTouchEvent(event);
            if (processed) {
                capturingTouchable = controller;
                Log.d(TAG, "Capture Start: " + capturingTouchable);
                return true;
            }
        }

        return false;
    }

    public void onEnter() {
        Log.v(TAG, "onEnter: " + getClass().getSimpleName());
    }
    public void onPause() {
        Log.v(TAG, "onPause: " + getClass().getSimpleName());
    }
    public void onResume() {
        Log.v(TAG, "onResume: " + getClass().getSimpleName());
    }
    public void onExit() {
        Log.v(TAG, "onExit: " + getClass().getSimpleName());
    }

    public boolean onBackPressed() {
        return false;
    }

    public boolean clipsRect() {
        return true;
    }

    public boolean isTransparent() {
        return false;
    }

    public void addController(ITouchable controller) {
        controllers.add(controller);
    }

    public void removeController(ITouchable controller) {
        controllers.remove(controller);
    }
}
