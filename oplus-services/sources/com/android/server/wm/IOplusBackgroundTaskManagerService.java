package com.android.server.wm;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.os.Parcel;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IOplusBackgroundTaskManagerService extends IOplusCommonFeature {
    public static final IOplusBackgroundTaskManagerService DEFAULT = new IOplusBackgroundTaskManagerService() { // from class: com.android.server.wm.IOplusBackgroundTaskManagerService.1
    };
    public static final String NAME = "IOplusBackgroundTaskManagerService";

    default boolean isScreenOffPlay(Task task) {
        return false;
    }

    default boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
        return false;
    }

    default void setScreenOffPlay(boolean z) {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusBackgroundTaskManagerService;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
