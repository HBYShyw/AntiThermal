package com.android.server.storage;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import java.io.PrintWriter;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IOplusStorageAllFileAccessManager extends IOplusCommonFeature {
    public static final IOplusStorageAllFileAccessManager DEFAULT = new IOplusStorageAllFileAccessManager() { // from class: com.android.server.storage.IOplusStorageAllFileAccessManager.1
    };
    public static final String NAME = "IOplusStorageAllFileAccessManager";

    default boolean checkAppWhitelist(String str, int i) {
        return false;
    }

    default ArrayList<Integer> computeGidsForOplus(int i, int i2) {
        return null;
    }

    default void dump(PrintWriter printWriter) {
    }

    default void initArgs(Context context) {
    }

    default void servicesReady() {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusStorageAllFileAccessManager;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
