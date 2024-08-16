package com.android.server.am;

import android.content.Context;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBroadcastProcessQueueExt {
    public static final int REASON_OPLUS_LIST = 101;
    public static final String REASON_OPLUS_LIST_NAME = "OPLUS_LIST";

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface IStaticExt {
        default String reasonToStringExtend(int i) {
            return null;
        }
    }

    default void assertHealthLocked(BroadcastRecord broadcastRecord, long j, ArrayList<String> arrayList) {
    }

    default ArrayList<String> beginAssertHealthLocked() {
        return null;
    }

    default void endAssertHealthLocked(Context context) {
    }

    default long getCustomizedRunnableAt(long j) {
        return Long.MAX_VALUE;
    }

    default void setCustomizedRunnableAtDelayMillis(long j) {
    }
}
