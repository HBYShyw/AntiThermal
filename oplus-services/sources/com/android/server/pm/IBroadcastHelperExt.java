package com.android.server.pm;

import android.content.IIntentReceiver;
import android.content.Intent;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IBroadcastHelperExt {
    default void afterBroadcastInDoSendBroadcast(Intent intent, String str, int i, String str2, IIntentReceiver iIntentReceiver) {
    }

    default void beforeBroadcastInDoSendBroadcast(Intent intent, String str) {
    }
}
