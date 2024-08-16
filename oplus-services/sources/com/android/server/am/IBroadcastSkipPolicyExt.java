package com.android.server.am;

import android.content.pm.ResolveInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBroadcastSkipPolicyExt {
    default String shouldSkipMessage(BroadcastRecord broadcastRecord, ResolveInfo resolveInfo) {
        return null;
    }

    default String shouldSkipMessage(BroadcastRecord broadcastRecord, BroadcastFilter broadcastFilter) {
        return null;
    }
}
