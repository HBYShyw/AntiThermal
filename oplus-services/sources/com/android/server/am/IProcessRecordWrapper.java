package com.android.server.am;

import android.util.IntArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IProcessRecordWrapper {
    default IntArray getHwuiTaskThreads() {
        return null;
    }

    default long getLastActivityTime() {
        return 0L;
    }

    default int getRenderThreadTid() {
        return 0;
    }

    default IProcessRecordExt getExtImpl() {
        return new IProcessRecordExt() { // from class: com.android.server.am.IProcessRecordWrapper.1
        };
    }
}
