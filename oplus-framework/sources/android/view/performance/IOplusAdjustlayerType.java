package android.view.performance;

/* loaded from: classes.dex */
public interface IOplusAdjustlayerType {
    public static final IOplusAdjustlayerType DEFAULT = new IOplusAdjustlayerType() { // from class: android.view.performance.IOplusAdjustlayerType.1
    };

    default void adjustImageViewLayerType(int width, int height) {
    }

    default boolean checkMutiTouchView() {
        return false;
    }

    default void adjustPendingLayertype(int layerType) {
    }
}
