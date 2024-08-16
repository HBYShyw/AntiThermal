package com.android.server.am;

import android.util.SparseIntArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IPendingIntentControllerExt {
    default void addPendingIntentUid(int i, String str, int i2) {
    }

    default void deletePendingIntentUid(int i, String str, int i2) {
    }

    default void killPendingApplicationIfNeed(int i, PendingIntentRecord pendingIntentRecord, PendingIntentController pendingIntentController, SparseIntArray sparseIntArray) {
    }

    default void recyclePendingIntentsIfNeed(PendingIntentController pendingIntentController, SparseIntArray sparseIntArray) {
    }

    default void removePendingIntentUid(int i) {
    }
}
