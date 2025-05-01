package kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects;

import java.util.ArrayList;
import java.util.HashMap;

public class ObjectRecycler {
    protected HashMap<Class, ArrayList<IRecyclable>> recycleBin = new HashMap<>();


    public void collectRecyclable(IRecyclable object) {
        Class clazz = object.getClass();
        ArrayList<IRecyclable> bin = recycleBin.get(clazz);
        if (bin == null) {
            bin = new ArrayList<>();
            recycleBin.put(clazz, bin);
        }
        //object.onRecycle(); // 객체가 재활용통에 들어가기 전에 정리해야 할 것이 있다면 여기서 한다
        bin.add(object);
        // Log.d(TAG, "collect(): " + clazz.getSimpleName() + " : " + bin.size() + " objects");
    }

    public <T extends IRecyclable> T getRecyclable(Class<T> clazz) {
        ArrayList<IRecyclable> bin = recycleBin.get(clazz);
        if (bin == null || bin.isEmpty()) {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        // Log.d(TAG, "get(): " + clazz.getSimpleName() + " : " + (bin.size() - 1) + " objects");
        return clazz.cast(bin.remove(0));
    }
}
