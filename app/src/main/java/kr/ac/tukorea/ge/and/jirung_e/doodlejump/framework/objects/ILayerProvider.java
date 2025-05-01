package kr.ac.tukorea.ge.and.jirung_e.doodlejump.framework.objects;

public interface ILayerProvider<E extends Enum<E>> extends IGameObject {
    public E getLayer();
}
