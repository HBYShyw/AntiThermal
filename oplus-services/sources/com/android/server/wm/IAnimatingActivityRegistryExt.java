package com.android.server.wm;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IAnimatingActivityRegistryExt {
    default void makeRunnableList(LinkedHashMap<ActivityRecord, Runnable> linkedHashMap, ArrayList<Runnable> arrayList) {
    }

    default boolean shouldDeferAnimatingActivityFinished(ActivityRecord activityRecord) {
        return false;
    }
}
