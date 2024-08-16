package com.android.server.appop;

import android.util.SparseArray;
import com.android.server.am.ProcessList;
import java.io.PrintWriter;
import java.util.concurrent.Executor;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface AppOpsUidStateTracker {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface UidStateChangedCallback {
        void onUidStateChanged(int i, int i2, boolean z);
    }

    static int processStateToUidState(int i) {
        if (i == -1) {
            return ProcessList.PREVIOUS_APP_ADJ;
        }
        if (i <= 1) {
            return 100;
        }
        if (i <= 2) {
            return 200;
        }
        if (i <= 3) {
            return 500;
        }
        if (i <= 4) {
            return 400;
        }
        if (i <= 5) {
            return 500;
        }
        if (i <= 11) {
            return 600;
        }
        return ProcessList.PREVIOUS_APP_ADJ;
    }

    void addUidStateChangedCallback(Executor executor, UidStateChangedCallback uidStateChangedCallback);

    void dumpEvents(PrintWriter printWriter);

    void dumpUidState(PrintWriter printWriter, int i, long j);

    int evalMode(int i, int i2, int i3);

    int getUidState(int i);

    boolean isUidInForeground(int i);

    void removeUidStateChangedCallback(UidStateChangedCallback uidStateChangedCallback);

    void updateAppWidgetVisibility(SparseArray<String> sparseArray, boolean z);

    void updateUidProcState(int i, int i2, int i3);
}
