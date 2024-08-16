package com.android.server.content;

import android.content.ComponentName;
import android.content.Context;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface ISyncManagerExt {
    default void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, boolean z) {
    }

    default void init(Context context) {
    }

    default boolean interceptDispatchSyncOperation(int i, SyncOperation syncOperation, ComponentName componentName) {
        return false;
    }

    default boolean isSyncValid(int i, String str) {
        return true;
    }

    default void onBootPhase(int i) {
    }
}
