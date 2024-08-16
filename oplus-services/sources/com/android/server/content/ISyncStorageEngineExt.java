package com.android.server.content;

import android.content.Context;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface ISyncStorageEngineExt {
    default void init(Context context) {
    }

    default boolean isDataSyncDisabled() {
        return false;
    }
}
