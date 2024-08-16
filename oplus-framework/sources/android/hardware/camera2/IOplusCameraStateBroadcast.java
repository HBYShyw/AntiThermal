package android.hardware.camera2;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;

/* loaded from: classes.dex */
public interface IOplusCameraStateBroadcast extends IOplusCommonFeature {
    public static final IOplusCameraStateBroadcast DEFAULT = new IOplusCameraStateBroadcast() { // from class: android.hardware.camera2.IOplusCameraStateBroadcast.1
    };
    public static final String NAME = "IOplusCameraStateBroadcast";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusCameraStateBroadcast;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void notifyCameraState(Context context, int cameraState, String clientName, int facing) {
    }
}
