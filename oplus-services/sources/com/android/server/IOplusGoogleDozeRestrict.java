package com.android.server;

import android.app.ActivityManagerInternal;
import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.SparseBooleanArray;
import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusGoogleDozeRestrict extends IOplusCommonFeature {
    public static final IOplusGoogleDozeRestrict DEFAULT = new IOplusGoogleDozeRestrict() { // from class: com.android.server.IOplusGoogleDozeRestrict.1
    };
    public static final String NAME = "IOplusGoogleDozeRestrict";

    default void initArgs(Context context, Handler handler, DeviceIdleController deviceIdleController) {
    }

    default boolean interceptWhitelistOperation(ApplicationInfo applicationInfo, String str, boolean z, boolean z2, boolean z3) {
        return false;
    }

    default void interceptWhitelistReset(boolean z, ArraySet<String> arraySet) {
    }

    default void reportWhitelistForAms(ActivityManagerInternal activityManagerInternal, SparseBooleanArray sparseBooleanArray, SparseBooleanArray sparseBooleanArray2) {
    }

    default void restoreConfigFile(ArrayMap<String, Integer> arrayMap, XmlSerializer xmlSerializer) throws IOException {
    }

    default void updateWhitelist() {
    }

    default void updateWhitelistApps(ArrayMap<String, Integer> arrayMap, boolean z, boolean z2) {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusGoogleDozeRestrict;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
