package com.android.server.location.eventlog;

import android.location.util.identity.CallerIdentity;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ILocationEventLogWrapper {
    default void addExtLog(Object obj) {
    }

    default void addLogToProviderEvent(String str, CallerIdentity callerIdentity, Object obj, long j, boolean z) {
    }

    default void updateEventsLocationSize(int i) {
    }
}
